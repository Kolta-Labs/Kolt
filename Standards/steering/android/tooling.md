# Enforcement tooling: Konsist, Detekt, ktlint

This steering set is long — an agent mid-task can miss a rule buried in the
middle of a doc, or apply it inconsistently across files. These three tools
turn the highest-value rules above into a build failure instead of a hope:

| Tool | Catches | Runs |
|---|---|---|
| [Konsist](#konsist-architecture-as-a-test) | Rules 1, 2, 3, 4, 10 | `test` source set, part of `check` |
| [Detekt](#detekt-custom-rules) | Rules 5, 6, 7, 8, 9, 11 (hardcoded tokens) | `./gradlew detekt`, part of `check` |
| Kotlin compiler | Rule 13 (deprecated Kolt token use) | compile, i.e. every build |
| [ktlint](#ktlint-formatting) | Formatting only — imports, spacing, trailing commas | pre-commit + `check` |

If a rule isn't listed as enforced by one of these, it isn't — reviewers and
the agent itself are still the only check on it.

## Konsist: architecture as a test

[Konsist](https://docs.konsist.lemonappdev.com/) asserts package/layer
structure by reading the Kotlin PSI tree — no reflection, no runtime, just a
plain JVM test file that fails like any other test. Put it in its own module
(`architecture-test`) so it scans every module without living inside one of
them.

```kotlin
// architecture-test/src/test/kotlin/ArchitectureTest.kt
class ArchitectureTest {
    private val codebase = Konsist.scopeFromProject()

    @Test
    fun `domain has no platform imports`() {
        codebase
            .classes()
            .filter { it.resideInPackage("..domain..") }
            .assertFalse {
                it.hasImports { imp ->
                    imp.name.startsWith("android.") || imp.name.startsWith("androidx.")
                }
            }
    }

    @Test
    fun `UseCase depends only on Repository interfaces, never another UseCase`() {
        codebase
            .classes()
            .withNameEndingWith("UseCase")
            .assertFalse {
                it.hasImports { imp -> imp.name.contains("UseCase") && !imp.name.endsWith(it.name) }
            }
    }

    @Test
    fun `RepositoryImpl depends only on its DataSource, never another Repository`() {
        codebase
            .classes()
            .withNameEndingWith("RepositoryImpl")
            .assertFalse {
                it.hasImports { imp -> imp.name.contains("Repository") && !imp.name.contains("DataSource") }
            }
    }

    @Test
    fun `DataSource never depends on another DataSource`() {
        codebase
            .classes()
            .withNameEndingWith("DataSource")
            .assertFalse {
                it.hasImports { imp -> imp.name.endsWith("DataSource") && !imp.name.endsWith(it.name) }
            }
    }

    @Test
    fun `Contract file declares State data class plus sealed Intent and Effect`() {
        codebase.files().withNameEndingWith("Contract.kt").assertTrue { file ->
            file.classes().any { it.name.endsWith("State") && it.hasDataModifier } &&
                file.interfaces().any { it.name.endsWith("Intent") && it.hasSealedModifier } &&
                file.interfaces().any { it.name.endsWith("Effect") && it.hasSealedModifier }
        }
    }

    @Test
    fun `every presentation file lives in a screen subpackage, not the bare presentation package`() {
        codebase
            .files()
            .filter { it.packagee?.name?.contains(".presentation") == true }
            .assertFalse { it.packagee?.name?.substringAfterLast('.') == "presentation" }
    }

    @Test
    fun `exactly one NavDisplay call exists in the whole app`() {
        val navDisplayCalls = codebase.functions().flatMap { it.functionCalls() }
            .filter { it.name == "NavDisplay" }
        assertEquals(1, navDisplayCalls.size)
    }
}
```

These cover non-negotiables 1, 2, 3, 4, and 10. Konsist checks import
graphs, package layout, and declaration shape — not "is this the shortest
diff." Don't write a Konsist test for a rule that's really a Detekt or
human-review concern. (KMP's set adds two more Konsist checks —
`commonMain`/`commonTest` source-set constraints this Android-only project
doesn't have.)

## Detekt: custom rules

[Detekt](https://detekt.dev/) covers what Konsist's import-graph model can't
reach: literal values and specific call patterns inside a file.

**Rule 5 (Nav3 only) needs no custom rule at all** — it's Detekt's stock
`ForbiddenImport` check, config only:

```yaml
# detekt.yml
imports:
  ForbiddenImport:
    active: true
    imports:
      - 'androidx.navigation.compose.*'
```

Four rules worth authoring as custom (`RuleSetProvider`), enforcing
non-negotiables 6, 7, 8, 9, and 11:

- **No hardcoded `Color(...)`/`.dp`/`.sp` outside the theme module** (rule 11)
  — enforces [theming.md](theming.md)'s no-hardcoded-token rule.
- **No plain `kotlin.collections.List`/`Map`/`Set` in a class named `State`**
  (rule 9) — enforces `ImmutableList`/`Map`/`Set` in MVI `State`.
- **No composable named `*Screen` taking a `ViewModel`, `NavBackStack`, or
  `Flow`-typed parameter** (rule 8) — the stateless root composable must stay
  `@Preview`-able with fake data.
- **`ViewModel` construction (via `hiltViewModel()` too) and
  `effect.collect`/`.launchIn` on an effect `Flow` only inside a file named
  `*Route.kt`** (rules 6, 7) — flag either call site elsewhere. This one is a
  naming heuristic, not a type check — it false-positives on an
  unconventional file name rather than a real violation; treat a hit as "go
  look," not an automatic fail-the-PR blocker the way the others are.

**Don't author these as a project-local rule set.** They're published once,
KMP-authored, as `io.github.koltalabs.kolt:detekt-rules` — see
[kolt-libs.md](kolt-libs.md#external-tooling-coordinates) for the coordinate
and consumption mode; same module the KMP steering set uses, one
implementation instead of two. Writing a project-local `RuleSetProvider` for
any of these duplicates a module that already exists; only fall back to a
project-local rule when `Kolt/libs` genuinely isn't present, or for a check
specific to that one project.

Everything else — indentation, naming conventions, complexity thresholds —
use Detekt's stock rule sets (`standard`, `complexity`, `exceptions`) via a
`detekt.yml` baseline; don't hand-roll a rule for something the default
config already flags.

## Compiler: deprecated Kolt tokens (rule 13)

Don't write a lint rule for this — Kolt marks a superseded token
`@Deprecated(level = DeprecationLevel.ERROR)` at the source, so
`Kolt.typography.textXLarge` is a compile error, not a warning an agent can
scroll past. Stronger than Detekt and free: nothing to configure per
project. If a deprecated-token usage compiles, the token isn't actually
marked `ERROR`-level in Kolt yet — fix it there, not with a new rule here.

## ktlint: formatting

[ktlint](https://pinterest.github.io/ktlint/) owns formatting only — import
order, trailing commas, spacing — never architecture. Run it via the
`org.jlleitschuh.gradle.ktlint` Gradle plugin, `ktlintFormat` as a pre-commit
hook (autofix, don't just report), `ktlintCheck` wired into `check` so CI
fails on unformatted code instead of an agent hand-fixing spacing.

## Wiring into `check`

```kotlin
// root build.gradle.kts
tasks.named("check") {
    dependsOn(":architecture-test:test", "detekt", "ktlintCheck")
}
```

One command (`./gradlew check`) is the actual enforcement — an agent that
skips reading a rule mid-doc still hits it here before the change lands.
