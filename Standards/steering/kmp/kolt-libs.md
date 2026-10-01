# Kolt/libs — what's reusable and what isn't (optional)

**This whole file only applies if `Kolt/libs` is actually present in the
workspace.** Nothing elsewhere in this steering set requires it — every
pattern it documents (theme module, `AsyncState`, `MviViewModel`) is defined
standalone in [theming.md](theming.md) / [presentation-mvi.md](presentation-mvi.md)
and works with zero Kolt dependency. That's the only thing optional here —
*whether Kolt/libs exists in this workspace at all.* Once it's present, using
what's in [Reuse as-is](#reuse-as-is-mandatory-when-present) below is not
optional: don't write a new `KoltButton`, a new theme module, a new
`AsyncState`, etc. once a vetted one already exists a few files over — that's
exactly the duplication [architecture.md](architecture.md#reuse-over-duplication)
forbids. "Evaluated on its actual merits" still applies in full, though:
mandatory means *use the parts that passed the check below*, not *trust
everything because it's sitting in the repo* — the `Don't reuse`/`Dead code`
sections are exclusions from that mandate, not suggestions.

`Kolt/libs` lives at `$KOLT_LIBS_PATH` (the local KoltLibs checkout; set it in `~/.gradle/gradle.properties` or ask the user). It's marketed
as a KMP util collection, but not every module in it actually is KMP. Checked
each module's source-set layout and dependencies before recommending
anything — don't assume "it's in the KMP repo" means "it works on iOS."

## How to consume it — ask once, at project creation

*(In the `Tech/KMP` workspace this is already decided: composite build — see
[Composite-build rules](#composite-build-rules-mandatory-when-kolt-is-an-includebuild). Only ask for
a project outside it.)*

Before wiring the first Kolt dependency into a new project, check whether
consumption is already decided: look for an `includeBuild(...)` pointing at
`Kolt/libs` in `settings.gradle.kts`, a copied module directory, or an
`io.github.koltalabs.kolt` coordinate in the version catalog. If none of those exist
yet, **ask the user which mode to use — always, exactly once per project,
never silently default:**

1. **Composite build / direct link from disk** — `includeBuild("<path-to-KoltLibs>")` in `settings.gradle.kts`. Not a copy: a live reference to the same files on this machine, edits in Kolt show up immediately with no publish step. Only works on this machine, at this path.
2. **Copy the module(s) in** — vendor the source into this project's tree. Portable across machines/CI, but you own drift from the source from that point on; no free updates.
3. **Gradle dependency on a published artifact** — `implementation("io.github.koltalabs.kolt:<module>:<version>")` (note: `compose-utils` is published under artifact ID `compose`, i.e. `io.github.koltalabs.kolt:compose:<version>`). Requires Kolt actually published (Maven local or remote) under that coordinate. Most portable/CI-friendly option, if publishing is set up.

Once answered, treat it as decided for the life of the project — don't
re-ask on later tasks; re-check the project state above instead.

## Composite-build rules (mandatory when Kolt is an `includeBuild`)

Any project in this workspace that consumes Kolt does so as a composite build — apps and
shared libraries alike — and **option 1 above is already decided**, don't re-ask. This
applies to every link in a dependency chain (app → library → library …), not just apps:
a *chained build* means any included build that itself applies a Kolt plugin.

1. **No versioned/published Kolt coordinates.** Never `io.github.koltsystems…`,
   `io.github.appspiriment…`, or a literal `io.github.koltalabs.kolt:<module>:<version>` that
   only resolves from `~/.m2`/Maven. Declare Kolt libraries through the version catalog and let
   the included build *substitute* them.
2. **Apply Kolt plugins by raw ID, with no version** — `id("io.github.koltalabs.kolt.kmp.library")`,
   never a catalog `alias(...)` with `version.ref`, and never `alias(...) apply false` for them in
   the root build. A version makes Gradle demand a published artifact.
3. **Every build that uses Kolt includes both parts in its own `settings.gradle.kts`:**
   `includeBuild("<path>/KoltLibs/build-logic")` (plugins) and
   `includeBuild("<path>/KoltLibs") { dependencySubstitution { … } }` (libraries), with a
   substitution for **every** Kolt module the plugins inject (`kolt-bom`, `utils`, `logutils`,
   `compose`, `compose-kmp`, `location`, `location-picker`). Relative paths. This is what lets
   each link in a chain build standalone.
4. **Depend on sibling projects by source, not by published jar.** If project A depends on
   project B (same workspace), `includeBuild` B with a `dependencySubstitution` for B's
   coordinate. A published B still carries whatever Kolt coordinate it was built with — the
   stale-coordinate problem, one hop removed.
5. **Include order is significant: chained builds first, Kolt last.** In any build that includes
   other builds which themselves use Kolt, every such `includeBuild` must appear *before* the two
   Kolt `includeBuild`s. Reversed, the chained builds fail plugin lookup ("None of the included
   builds contain this plugin"). This holds for however deep the chain goes: the build at the
   top of a chain lists its dependencies first, Kolt at the very end. The cause isn't
   understood — found empirically, and gating the chained builds' own Kolt includes on
   `gradle.parent == null` made it worse — so treat it as a hard rule and keep a comment beside
   the includes stating it.
6. **Prove it before calling it done:** temporarily move
   `~/.m2/repository/io/github/{koltalabs,koltsystems,appspiriment}` aside and run the project's
   tests / assemble with `--offline`. If it only builds with those present, something is still
   resolving from Maven.

## External tooling coordinates

[tooling.md](tooling.md)'s enforcement stack, pinned:

```toml
# gradle/libs.versions.toml
[versions]
konsist = "0.17.3"
detekt = "1.23.7"
ktlintGradle = "12.1.1"

[libraries]
konsist = { module = "com.lemonappdev:konsist", version.ref = "konsist" }

[plugins]
detekt = { id = "io.gitlab.arturbosch.detekt", version.ref = "detekt" }
ktlint = { id = "org.jlleitschuh.gradle.ktlint", version.ref = "ktlintGradle" }
```

Konsist is a plain JVM test dependency (`testImplementation`), not a Gradle
plugin. Bump these against each tool's own release notes before pinning —
don't carry stale versions forward from this doc indefinitely.

**Detekt custom rules live in Kolt, not per-app.** The two rules
[tooling.md](tooling.md#detekt-custom-rules) specifies (no hardcoded
`Color`/`.dp`/`.sp`, no plain collection in a `State`) are published once as
`io.github.koltalabs.kolt:detekt-rules:<version>` and consumed the same way
as any other Kolt module (see [How to consume it](#how-to-consume-it--ask-once-at-project-creation)
above) — writing a project-local `RuleSetProvider` for either of these two
duplicates a module that already exists a few files over, exactly the
[architecture.md](architecture.md#reuse-over-duplication) violation this
whole doc exists to prevent. Only author a project-local Detekt rule for a
check that's specific to that project, not one already covered by
`detekt-rules`.

## Reuse as-is (mandatory when present)

If `Kolt/libs` is in the workspace, use these — don't hand-write a new
equivalent and don't treat this as a "pick whichever you feel like" menu.

**Check the resolved version before reaching for a recently-added slot.**
Some tokens below are called out as added in a specific dev build (e.g.
`body.mediumLargeMid`/`body.xLargeMid` in 0.2.1-dev2, `size*`/`image*` in
0.2.1-dev3). If this project consumes Kolt via a pinned Gradle coordinate
rather than `includeBuild`/a copied module, confirm the pinned version
(version catalog / `build.gradle.kts`) actually includes a slot before using
it — an older pin won't have it, and the "always use Kolt tokens" mandate
isn't license to assume every slot documented here is present. If it's
missing, either bump the pin or fall back to the closest available slot,
don't hand-roll a literal.

**Tokens are part of the mandate.** Whenever Kolt is available (in the workspace, an `includeBuild`, a copied module, or an `io.github.koltalabs.kolt` dependency),
every color, padding/spacing, corner radius, icon/button size and text style
comes from `Kolt.colors` / `Kolt.sizes` / `Kolt.typography`
when Kolt has one — no literals, no duplicate app tokens, and no deprecated Kolt tokens (replace them with the current one when you touch the code). Rules in
[theming.md](theming.md#rules).

| Module | Path | Why it's safe |
|---|---|---|
| `compose-kmp` theme | `compose-kmp/src/{commonMain,androidMain,iosMain,desktopMain}/.../theme/` | Genuine `expect`/`actual` KMP. `Kolt` object + `LocalColors`/`LocalTypography`/`LocalSizes` + `CompositionBaseProvider` is exactly the pattern in [theming.md](theming.md). Text: `Kolt.typography.title.*` / `Kolt.typography.body.*` (full scale each, weight via `.bold` etc.), covering 6–48 sp incl. `body.mediumLargeMid` (17 sp) and `body.xLargeMid` (22 sp) — use a slot, not `TextStyle(fontSize = …)`. The old flat `Kolt.typography.text*` names for these same slots are deprecated, removed in 0.3.0 — don't reuse them even as an example; full scale in [theming.md](theming.md#kolt-type-scale). Sizes: `Kolt.sizes.padding*`/`cornerRadius*`/`icon*`/`size*`/`image*` — no raw `.dp` for anything on a scale; own `LocalSizes` starts from `KoltDefaults.sizes()`; families in [theming.md](theming.md#kolt-size-scale). |
| `compose-kmp` component library | `compose-kmp/src/commonMain/.../components/` — buttons, text/text fields, containers (card, accordion, tooltip, divider), messages (snackbar/banner/dialog), image, badges, progress, rating bar, slider, stepper, shimmer | `commonMain`-first; only `image/` drops to `androidMain`/`iosMain`/`desktopMain` `actual` (platform image loading — Coil on Android). This is the reusable-component target from [architecture.md](architecture.md#reuse-over-duplication) already built — check here before writing a new `KoltButton`/`KoltCard`/etc. |
| `utils` → `AsyncState` | `utils/src/commonMain/.../state/AsyncState.kt` | Plain `commonMain` sealed class (`Idle`/`Loading`/`Success`/`Error`) with `map`/`onSuccess`/`getOrElse`. Clean, no platform deps. Use it in `State` fields per [presentation-mvi.md](presentation-mvi.md). |
| `logutils` | `logutils/` | Real `commonMain`/`androidMain`/`desktopMain`/`nativeMain` KMP logging, auto debug/release gating on Android via App Startup. Fine to use as-is. |
| `location` | `location/` | Real KMP (`commonMain` + per-platform `actual`, including `wasmJs` via a hand-written `navigator.geolocation` binding — `kotlinx-browser` doesn't cover that API). Fine if a journey needs location. |
| `location-picker` | `location-picker/` | Real KMP (`commonMain` + android/ios/desktop/wasmJs `actual`). All-in-one Search/Map/Current-location/Manual-entry picker UI built on `location` — `LocationPicker.rememberLauncher`/`.present`/`.showDialog`/`.Embed` per platform. Fine to consume as a black-box UI dependency for a "pick a location" screen. Follows this steering set (MVI, `ImmutableList` state, `compose-kmp` theme, `commonTest` coverage); one documented `wasmJs` rendering gap; Android permission/services flow is self-contained, no extra `ACCESS_FINE_LOCATION` request needed from the consuming app — full detail in [kolt-libs-reference.md](kolt-libs-reference.md) if you're integrating it. |

Also in `kolt-libs-reference.md`: why not to import `compose-utils`'
`KoltBanner`/`KoltBottomSheet`/`KoltSnackbar`/`DialogButtonStyle`/
`MessageDialog`/`ColorUtils` (they're backward-compat stubs for the
`compose-kmp` versions above — import from `compose-kmp` directly).

## Don't reuse — Android-only despite living here

| Module | Path | Problem |
|---|---|---|
| `compose-utils` ViewModel bases | `compose-utils/.../utils/base/UiStateEventsViewModel.kt`, `UiStateEventsAndroidViewModel.kt`, `UiEventsViewModel.kt`, `UiEventsAndroidViewModel.kt` | Module has no `commonMain` at all — everything is under `src/main/java`. `UiStateEventsAndroidViewModel` extends `android.app.AndroidViewModel`. Building a real KMP presentation layer on these breaks the moment iOS is added. Use the `commonMain` `MviViewModel` in [presentation-mvi.md](presentation-mvi.md) instead. |
| `compose-utils` nav helpers | `KoltBottomNavigationNavHost.kt`, `KoltNavType.kt` | `KoltNavType.genericNavType` imports `android.net.Uri` directly. The module's `build.gradle.kts` pulls the Android `navigation-compose` artifact via an Android-only convention plugin, not the KMP coordinate. Not usable outside Android — and independent of that, it's built on Navigation 2 (`NavController`/`NavGraphBuilder`), which [navigation.md](navigation.md) has this project moving off in favor of Navigation 3. |
| `utils` → `FlowUtils.collectState` / `collectFlows` | `utils/src/commonMain/.../extensions/FlowUtils.kt` | *Is* commonMain, but the pattern itself is one to avoid: callback-listener style predating structured concurrency (`successListener`/`errorListener`/`onLoading` triads), and `collect()` bakes in a hardcoded `delay(200)` after every emission — every consumer inherits 200ms of invisible latency. Use `stateIn` + `collectAsStateWithLifecycle` instead. |
| `compose-utils` remaining components | dropdowns, `KoltTextField`/`KoltValidatedTextField`, `PageScaffold`, `KoltTopBar`, `KoltDrawerScaffold`, swipe-actions box, `Lottie`, `RememberSpeechToText`, photo picker | Genuinely useful, but Android-only (`src/main/java`, no `commonMain`) and *not* migrated to `compose-kmp` like the messages/ components were — no compat stub, no KMP equivalent exists yet. Fine to reuse only on a screen that will never ship iOS/desktop. Otherwise this is exactly the "extract once you need it" case from [architecture.md](architecture.md#reuse-over-duplication) — port the specific component you need into the shared KMP component module, don't block on porting all of them upfront. |

## Dead code — don't copy the pattern

`compose-utils/.../base/ViewModelDelegate.kt` is an unused internal class
that duplicates logic already reimplemented by hand elsewhere — two
divergent copies plus one unused third. Don't carry the inconsistency
forward; use the `MviViewModel` in [presentation-mvi.md](presentation-mvi.md).
Full trace in [kolt-libs-reference.md](kolt-libs-reference.md#dead-code--dont-copy-the-pattern).

## Not evaluated

`update-utils`, `bom` — out of scope for this steering set (app-update flow
and Maven BOM packaging, not presentation/theme/navigation). Check them on
their own merits if/when a journey needs in-app update prompts.
