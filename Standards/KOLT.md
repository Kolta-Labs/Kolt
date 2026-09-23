# Kolt Suite — Plugin & Library Reference

**Authority:** On-demand reference for consumer projects using Kolt convention plugins and libraries.
**Read this when:** you're wiring up a new module, using compose-utils components, configuring data layers, or debugging why a dependency isn't resolving.

Section map: §1 Quick-start · §2 Plugin catalog · §3 `kolt {}` DSL · §4 `kmp {}` DSL · §5 `dataLayer {}` DSL · §6 Libraries · §7 Version catalog aliases · §8 Scaffolding · §9 Dev workflow (UtilsLibs contributors)

---

## 1. Quick-start (new project)

**Fastest path — bootstrap script** (from the UtilsLibs repo):
```bash
# Android-only project
./scripts/new-project.sh --type android --name MyApp --package com.example.myapp

# KMP project
./scripts/new-project.sh --type kmp --name MyApp --package com.example.myapp

# Interactive (prompts for values)
./scripts/new-project.sh
```

Produces a complete project with Gradle files, wrapper, source dirs, and all docs. Then:
```bash
cd MyApp && ./gradlew :app:assembleDebug   # first build scaffolds theme XMLs
```

**Manual wiring** (for an existing project or adding a new module):
```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    versionCatalogs {
        create("koltlibs") {
            from("io.github.koltalabs.kolt:kolt-catalog:<version>")
        }
        // KMP projects also add:
        create("kmplibs") {
            from("io.github.koltalabs.kolt:kmp-catalog:<version>")
        }
    }
}
```
```kotlin
// app/build.gradle.kts
plugins { id("io.github.koltalabs.kolt.application") }
android { namespace = "com.example.myapp" }
```

First build auto-runs `scaffoldKoltResources` (theme XMLs) and `scaffoldKoltDocs` (docs/). Both are idempotent.

See `templates/android-project/` and `templates/kmp-project/` for full Gradle file sets.

---

## 2. Plugin catalog

All plugins share the same version (from `version.properties → MAJOR.dev-XX`).

### Android plugins (catalog: `koltlibs`)

| Plugin ID | Catalog alias | What it auto-wires |
|---|---|---|
| `io.github.koltalabs.kolt.application` | `kolt-application` | AGP application + Kotlin Android + Hilt + Compose + Compose compiler + stdlib + utils + logutils + Room-optional + debug/release build types with `.dev` suffix on debug |
| `io.github.koltalabs.kolt.library` | `kolt-library` | AGP library + Kotlin Android + stdlib + utils + logutils |
| `io.github.koltalabs.kolt.library-compose` | `kolt-library-compose` | above + Compose + Compose compiler |
| `io.github.koltalabs.kolt.library-hilt` | `kolt-library-hilt` | above (no Compose) + Hilt + KSP |
| `io.github.koltalabs.kolt.library-hilt-compose` | `kolt-library-hilt-compose` | AGP library + Kotlin + Hilt + KSP + Compose + Compose compiler |
| `io.github.koltalabs.kolt.data` | `kolt-data` | `library-hilt` baseline + opt-in Room / Retrofit / DataStore / Security / WorkManager via `dataLayer {}` |

### KMP plugins (catalog: `kmplibs`)

| Plugin ID | Catalog alias | What it auto-wires |
|---|---|---|
| `io.github.koltalabs.kolt.kmp.library` | `kmp-library` | AGP library + Kotlin Multiplatform (Android + optional iOS/Desktop/WASM) + Coroutines + stdlib + utils + logutils |
| `io.github.koltalabs.kolt.kmp.library-compose` | `kmp-library-compose` | above + Compose Multiplatform + Compose compiler |
| `io.github.koltalabs.kolt.kmp.library-koin` | `kmp-library-koin` | `kmp-library` + Koin KMP |
| `io.github.koltalabs.kolt.kmp.library-koin-compose` | `kmp-library-koin-compose` | all of the above + Compose Multiplatform |
| `io.github.koltalabs.kolt.kmp.data` | `kmp-data` | KMP library baseline + opt-in SQLDelight / Room KMP / Ktor / Ktorfit / Retrofit (Android-only) / DataStore / serialization via `kmpDataLayer {}` |
| `io.github.koltalabs.kolt.kmp.application` | `kmp-application` | Android application host for a KMP project — AGP application + Kotlin Android + compose UI launcher |

### Typical module-type mapping

| Module type | Plugin to use |
|---|---|
| Android app (host) | `application` |
| Android feature / UI | `library-hilt-compose` |
| Android domain (no UI, no DI) | `library` |
| Android data layer | `data` + `dataLayer {}` |
| KMP shared (VM + domain) | `kmp.library-koin` |
| KMP feature (CMP UI) | `kmp.library-koin-compose` |
| KMP data layer | `kmp.data` + `kmpDataLayer {}` |
| KMP app host | `kmp.application` |

---

## 3. `kolt {}` DSL (Android plugins)

Available on `application`, `library*`, and `data` plugins.

```kotlin
kolt {
    enableUtils.set(true)                          // default true — adds utils + logutils to implementation
    enableMinify.set(false)                        // default false — enables R8 on release

    // Debug applicationId suffix (so debug installs alongside release)
    addDevSuffixToDebug.set(true)                  // default true (app only) — master switch for the suffix
    debugApplicationIdSuffix.set(".dev")           // default ".dev" — override with your own, e.g. ".qa"

    // Debug versionName timestamp (independent of the suffix above)
    appendTimestampToDebugVersion.set(true)        // default true (app only) — toggle the timestamp on/off
    debugVersionTimestampPattern.set("yyyyMMdd-HHmm") // default "yyyyMMdd-HHmm" — any DateTimeFormatter pattern

    scaffoldThemeResources.set(true)               // default true (app + compose) — writes theme XMLs on first build
}
```

**`enableUtils = false`** — use when the module has zero logging/utility needs or you're testing in isolation.

**`addDevSuffixToDebug = false`** — use for white-label / SDK modules where the debug applicationId must stay clean. The suffix string itself is overridable via `debugApplicationIdSuffix` (e.g. `.qa`, `.internal`).

**`appendTimestampToDebugVersion = false`** — turn off the build timestamp on `versionName` (e.g. for reproducible debug builds or CI artifact naming). The format is configurable via `debugVersionTimestampPattern` — a debug build then reads e.g. `1.0.0.20260613-1430`.

> The applicationId suffix and the versionName timestamp are fully independent: you can keep one and drop the other.

---

## 4. `kmp {}` DSL (KMP plugins)

Available on `kmp.library*`, `kmp.data`, and `kmp.application` plugins.

```kotlin
kmp {
    enableIos.set(false)      // default false — adds iosArm64 + iosSimulatorArm64 + iosX64
    enableDesktop.set(false)  // default false — adds jvm "desktop" target
    enableWasm.set(false)     // default false — adds wasmJs (browser)
    enableUtils.set(true)     // default true — adds utils + logutils to commonMain
    enableMinify.set(false)   // default false (application only)

    // Debug applicationId suffix + versionName timestamp (kmp.application module only).
    // Same semantics and defaults as the Android `kolt {}` block.
    addDevSuffixToDebug.set(true)                     // default true — master switch for the suffix
    debugApplicationIdSuffix.set(".dev")              // default ".dev" — override, e.g. ".qa"
    appendTimestampToDebugVersion.set(true)           // default true — toggle the timestamp on/off
    debugVersionTimestampPattern.set("yyyyMMdd-HHmm") // default "yyyyMMdd-HHmm"
}
```

> The four debug knobs apply only to the `kmp.application` module (the Android host of a KMP
> project). On `kmp.library*` / `kmp.data` modules they are ignored — libraries have no applicationId.

**Source sets auto-created:**
- Always: `commonMain`, `androidMain`, `commonTest`, `androidUnitTest`
- `enableIos = true`: `iosMain` (shared across all three iOS targets)
- `enableDesktop = true`: `desktopMain`

---

## 5. `dataLayer {}` DSL (Android data plugin)

```kotlin
dataLayer {
    room {
        enabled.set(true)    // adds room-runtime, room-ktx, room-compiler (KSP)
        usePaging.set(false) // also add room-paging
    }
    retrofit {
        enabled.set(true)
        useChucker.set(true)                // Chucker debug / no-op release
        useKotlinSerialization.set(false)   // use kotlinx-serialization converter instead of Gson
    }
    security { enabled.set(false) }        // androidx.security.crypto
    dataStore { enabled.set(false) }       // DataStore Preferences
    workManager { enabled.set(false) }     // WorkManager
}
```

**KMP equivalent: `kmpDataLayer {}`** (same shape but with SQLDelight/Room-KMP/Ktor/Ktorfit/Retrofit):
```kotlin
kmpDataLayer {
    // Persistence — pick one
    sqlDelight { enabled.set(true) }       // SQLDelight 2.x — full KMP (recommended)
    room {                                 // Room 3.x KMP (alpha) — no wasmJs support
        enabled.set(true)
        schemaVersion.set(1)
        schemaDirectory.set("schemas")     // relative to module dir; "" = disable export
    }

    // Networking — pick one or more
    ktor { enabled.set(true); useLogging.set(false) }    // full KMP
    ktorfit { enabled.set(true); useLogging.set(false) } // Retrofit-style over Ktor; full KMP
    retrofit {                             // androidMainImplementation ONLY — not shared
        enabled.set(true)
        useChucker.set(true)
        useKotlinSerialization.set(false)  // false = Gson converter; true = kotlinx.serialization
    }

    dataStore { enabled.set(false) }
    serialization { enabled.set(false) }   // kotlinx.serialization standalone
}
```

---

## 6. Libraries

### 6.1 logutils (`io.github.koltalabs.kolt:logutils`)

KMP (commonMain + androidMain). Auto-added when `enableUtils = true`.

```kotlin
// One-time init (Application.onCreate or App Startup — auto-wired via LogInitializer):
Log.init(enabled = BuildConfig.DEBUG)

// Usage in any file (commonMain or androidMain):
"Fetching cart for user $id".printLog()
"Retry attempt $n".printLog(tag = "SyncEngine", level = LogLevel.WARN)
exception.message.printLog(isError = true, throwable = exception)
```

- **`LogInitializer`** (AndroidX App Startup) — automatically gates logs on `FLAG_DEBUGGABLE`; added to manifest by the library. No manual `Log.init` call needed in production builds.
- **`LogLevel`**: `VERBOSE`, `DEBUG`, `INFO` (default), `WARN`, `ERROR`.
- In `commonMain`, calls go through `expect fun printLog(...)`. In `androidMain`, maps to `android.util.Log`.

### 6.2 utils (`io.github.koltalabs.kolt:utils`)

KMP. Auto-added when `enableUtils = true`.

**`commonMain` (use in any source set):**
```kotlin
// Flow extensions
flow.stateInWhileSubscribed(scope)             // StateFlow with WhileSubscribed(5000)
flow.shareInWhileSubscribed(scope)             // SharedFlow same policy
flow.debounceAndCollect(300L) { ... }          // debounce + collect shorthand

// List extensions (ListUtils)
list.moveItem(fromIndex, toIndex)
list.replaceFirst(predicate) { transform(it) }

// String extensions (StringExtns)
string.isValidEmail()
string.isValidUrl()
string.capitalizeWords()

// Phone (PhoneNumberUtils)
PhoneNumberUtils.format("+919876543210")       // formatted display string
PhoneNumberUtils.isValid("+919876543210")
```

**`androidMain` only (import from `io.github.koltalabs.kolt.utils.extensions`):**
```kotlin
// Context
context.launchPlayStorePage("com.example.app")
context.isNetworkAvailable()

// FormatUtils
FormatUtils.formatCurrency(amount, currencyCode)
FormatUtils.formatDate(localDate, pattern)

// java.time extensions (LocalDate / LocalDateTime / ZonedDateTime)
localDate.toEpochMillis()
localDate.formatDisplay()            // e.g. "12 Jan 2026"
localDateTime.toRelativeTime()       // e.g. "3 hours ago"
millis.toLocalDateTime()
```

### 6.3 compose-utils (`io.github.koltalabs.kolt:compose`)

Android-only. Add explicitly or use a plugin that includes it.

**Wrapper types (use these instead of primitives in State/UiModel):**
```kotlin
UiText.DynamicString("hello")
UiText.StringResource(R.string.greeting, arg1)
uiText.asString(context)           // resolve in a composable via LocalContext

UiImage.Resource(R.drawable.ic_star, description = "Star")
uiImageResource(R.drawable.ic_star)   // convenience
uiImageResouce(R.drawable.ic_star)    // typo alias (kept for compatibility)

UiColor.from(Color.Red)
UiColor.fromResource(R.color.primary)
```

**MVI Base ViewModels (KMP-ready — `androidx.lifecycle` ≥ 2.8.0):**
```kotlin
// Standard: ViewModel with state + user intents + one-shot effects
class HomeViewModel : MviViewModel<HomeState, HomeIntent, HomeEffect>(HomeState()) {
    override suspend fun onIntent(intent: HomeIntent) = when (intent) {
        HomeIntent.Load        -> loadData()
        is HomeIntent.Select   -> sendEffect(HomeEffect.NavigateTo(intent.id))
        HomeIntent.ClearError  -> updateState { copy(error = null) }
    }

    private suspend fun loadData() {
        updateState { copy(isLoading = true) }
        val items = repo.getItems()
        updateState { copy(isLoading = false, items = items) }
    }
}

// Read-only screen (no user-driven intents):
class DetailViewModel : MviStateViewModel<DetailState, DetailEffect>(DetailState())
```

Collect in the Route composable:
```kotlin
val state by vm.state.collectAsStateWithLifecycle()
vm.collectEffects { effect ->
    when (effect) {
        is HomeEffect.NavigateTo -> navController.navigate(DetailRoute(effect.id))
    }
}
// dispatch user actions:
Button(onClick = { vm.dispatch(HomeIntent.Load) }) { Text("Refresh") }
```

The old classes (`UiStateEventsViewModel`, `UiEventsViewModel`, `UiStateEventsAndroidViewModel`)
are `@Deprecated(level = WARNING)` with `ReplaceWith` guidance — use `MviViewModel` for all new code.

**Scaffold & containers:**
```kotlin
KoltPageScaffold(
    title = AppBarTitle.Text(uiText),
    navigationMode = NavigationMode.Back,    // or .None .Drawer .Custom(icon, onClick)
    onNavigateBack = { ... },
    topBarButtons = listOf(KoltTopBarButton(...)),
    scaffoldColors = ScaffoldColors.default(),
) { /* ColumnScope content */ }

KoltDrawerScaffold(drawerContent = { ... }) { /* content */ }

TitledCardView(title = uiText) { /* content */ }

PullToRefreshBox(isRefreshing, onRefresh) { /* content */ }
```

**Buttons:**
```kotlin
KoltButton(text = uiText, buttonStyle = ButtonStyle.primary(), onClick = { ... })
TextButton(text = uiText, buttonStyle = ButtonStyle.transparent(), onClick = { ... })
KoltIconButton(icon = uiImage, onClick = { ... })
KoltImageButton(image = uiImage, onClick = { ... })
CircularButton(icon = uiImage, onClick = { ... })

// ButtonStyle factories (all @Composable):
ButtonStyle.primary()                    // filled primary
ButtonStyle.secondary()                  // outlined
ButtonStyle.transparent()                // text-only
ButtonStyle.primaryNegative()            // muted / de-emphasised
ButtonStyle.primaryPositive()            // accent / confirmed action
```

**Text:**
```kotlin
KoltText(text = uiText, style = Kolt.typography.body.medium, color = ...)
MalayalamText(text = uiText)             // same params, Malayalam-font override
KeyValuePairText(key = uiText, value = uiText)
PrefixedText(prefix = uiText, value = uiText)
KoltImageText(image = uiImage, text = uiText)
```

**Text fields:**
```kotlin
KoltTextField(value, onValueChange, label, ...)
KoltValidatedTextField(state = TextFieldState(...), onValueChange, ...)
// TextFieldState wraps value + error + ValidationRule list
```

**Dropdowns:**
```kotlin
KoltDropDown(items = listOf(DropDownItem(...)), selected, onSelected)
ChipDropDown(...)
DropDownSpinner(...)
TextDropDown(label, items, selected, onSelected)
IconDropDown(icon, items, selected, onSelected)
```

**Images:**
```kotlin
KoltImage(image = uiImage, modifier = ...)    // auto-handles resource/URL/painter
KoltIcon(image = uiImage, modifier = ...)     // tinted icon variant
```

**Progress & messages:**
```kotlin
FullscreenLoader(isVisible = state.isLoading)

MessageDialog(
    title = uiText, message = uiText,
    positiveButton = DialogButtonStyle(label = uiText, onClick = { ... }),
    negativeButton = DialogButtonStyle(label = uiText, onClick = { ... }),
    onDismiss = { ... }
)

// Collect one-shot effects (lifecycle-aware):
vm.collectEffects { effect -> when (effect) {
    is MyEffect.ShowToast  -> showToast(effect.msg)
    is MyEffect.NavigateTo -> navController.navigate(effect.route)
} }
```

**Other:**
```kotlin
KoltBottomSheet(visible, onDismiss) { /* SheetContent */ }

// Swipe actions list item:
SwipeableActionsBox(
    startActions = listOf(SwipeAction(icon = ..., background = Color.Red, onSwipe = { delete() })),
) { /* item content */ }

// Modifier
Box(Modifier.shimmerEffect())   // loading shimmer

// Animation
AnimatedComposable(visible = ...) { /* content */ }

// Bottom navigation
KoltBottomNavigation(items = navItems, navController = navController)
KoltBottomNavigationNavHost(navController, startDestination, items) { /* route composables */ }

// Speech to text
val speechState = rememberSpeechToText()

// Theme tokens
Kolt.colors.primary
Kolt.colors.onMainSurface
Kolt.colors.accentedBlueText
Kolt.typography.body.medium          // body set, any size
Kolt.typography.title.xLarge.bold    // title set + weight extension
Kolt.typography.body.mediumLargeMid  // raw size step, when no role fits
Kolt.sizes.paddingMedium
```

**Own `LocalSizes`** (theme bridge onto an app palette) — start from Kolt's scale and
override only what differs, never a bare `Sizes(...)` (unset fields are `Unspecified`):
`LocalSizes provides KoltDefaults.sizes().copy(paddingLarge = 24.dp)`.

**Element sizes** — `Kolt.sizes.sizeX` for non-icon dimensions (button/row/bar heights,
avatars, dots); `icon*` stays for icons:

| 8 | 16 | 24 | 32 | 36 | 40 | 48 | 56 | 64 | 80 | 96 | 128 |
|---|---|---|---|---|---|---|---|---|---|---|---|
| XXXSmall | XXSmall | XSmall | Small | SmallMedium | Medium | MediumLarge | Large | XLarge | XXLarge | XXXLarge | Giant |

**Image sizes** — `Kolt.sizes.imageX` for avatars, thumbnails and illustrations:

| 24 | 32 | 40 | 48 | 56 | 64 | 80 | 96 | 120 | 160 | 200 | 240 | 320 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| XXSmall | XSmall | Small | SmallMedium | SmallMediumPlus | Medium | MediumLarge | Large | XLarge | XXLarge | XXXLarge | Huge | Giant |

**Type scale** — `Kolt.typography.body.x` / `title.x` is built from `Kolt.sizes.fontSizeX`
(Android `values/dimens.xml` / desktop values, sp):

| 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 20 | 22 | 24 | 28 | 32 | 36 | 40 | 48 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| Minimum | Tiny | XXXSmall | XXSmall | XSmall | XSmallMedium | Small | SmallMedium | Medium | MediumMid | MediumLarge | **MediumLargeMid** | Large | XLarge | **XLargeMid** | XXLarge | XXXLarge | Big | XBig | Huge | Giant |

**Title / body sets** — `Kolt.typography.title` and `Kolt.typography.body` each hold
this whole scale with short names (`title.xLarge`, `body.xSmall`, …); add weight
with `.bold`, `.light`, etc. `CompositionBaseProvider` also fills
`MaterialTheme.typography` from this scale. The flat `Kolt.typography.bodyMedium`/
`titleLarge`/… aliases are deprecated.

`mediumLargeMid` (17 sp) and `xLargeMid` (22 sp) were added in
compose-kmp `0.2.1.dev-02`, appended at the end of the `Sizes`/`UiSizes`/
`BaseTextStyles` constructors so nothing existing moved. Use a slot for any
size on the scale; a raw `TextStyle(fontSize = …)` only for runtime-computed
sizes, with a one-line comment saying why.

### 6.4 update-utils (`io.github.koltalabs.kolt:update-utils`)

Android-only. Uses Firebase Remote Config to gate forced/optional updates.

```kotlin
// In your app-level ViewModel (delegation pattern):
class MainViewModel @Inject constructor(...) :
    MviViewModel<MainState, MainIntent, MainEffect>(MainState()),
    AppUpdateHelperUtil by AppUpdateHelperUtilImpl() {

    override suspend fun onIntent(intent: MainIntent) { ... }
}

// In your root composable (MainActivity → NavHost entry):
@Composable
fun AppRoot(vm: MainViewModel = hiltViewModel()) {
    vm.CheckForUpdateAndSetContent(
        content = { /* your main NavHost / screens */ },
        onForceUpdate = { /* open Play Store */ },
    )
}
```

**Remote Config keys (set in Firebase Console):**
- `CRITICAL_UPDATE` — `true` shows a non-dismissable update dialog
- `FEATURE_DROP` — `true` shows a dismissable "new version available" dialog

---

## 7. Version catalog — versions & plugins only

Both catalogs (`koltlibs`, `kmplibs`) contain **only `[versions]` and `[plugins]`** — there
are no library or bundle aliases. Library *coordinates* live inside the convention plugins, which
add the right dependencies for you when you apply a plugin. You therefore almost never reference a
library through the catalog.

**What the plugin adds for you (no declaration needed):**
- Apply `io.github.koltalabs.kolt.library` → core Android + utils + logutils + the unit-test stack.
- Apply `io.github.koltalabs.kolt.library-compose` → all of the above **plus** the Compose UI stack,
  compose-utils, lottie, and hilt-navigation-compose.
- Apply `*-hilt*` → Hilt runtime + compiler. Apply `kmp.data` with a `kmpDataLayer {}` block →
  Room / SQLDelight / Ktor / Ktorfit / Retrofit / DataStore as you opt in.
- All `io.github.koltalabs.kolt:*` artifacts are pinned by the Kolt **BOM** the plugin
  injects — so if you ever add one by hand, omit the version.

### Plugins (the only catalog accessors you normally use)

```kotlin
// koltlibs (Android — both kolt.* and legacy kolt.* accessors supported)
alias(koltlibs.plugins.kolt.application)       // or alias(koltlibs.plugins.kolt.application)
alias(koltlibs.plugins.kolt.library)
alias(koltlibs.plugins.kolt.library.compose)
alias(koltlibs.plugins.kolt.library.hilt)
alias(koltlibs.plugins.kolt.library.hilt.compose)
alias(koltlibs.plugins.kolt.data)

// kmplibs (KMP — supports kmp.*, kolt.kmp.*, and kolt.kmp.* accessors)
alias(kmplibs.plugins.kmp.library)              // or alias(kmplibs.plugins.kolt.kmp.library)
alias(kmplibs.plugins.kmp.library.compose)
alias(kmplibs.plugins.kmp.library.koin)
alias(kmplibs.plugins.kmp.library.koin.compose)
alias(kmplibs.plugins.kmp.data)
alias(kmplibs.plugins.kmp.application)          // or alias(kmplibs.plugins.kolt.kmp.application)
```

### Adding a third-party lib the plugin doesn't provide

Declare the coordinate directly and pull the version from the catalog's `[versions]`:

```kotlin
dependencies {
    implementation("io.coil-kt:coil-compose:${'$'}{koltlibs.versions.coil.get()}")
    // An Kolt artifact added by hand needs no version — the BOM pins it:
    implementation("io.github.koltalabs.kolt:update-utils")
}
```

> Run `./gradlew :app:dependencies` to see what was auto-added vs what you need to add explicitly.

---

## 8. Scaffolding

### Theme resources (`scaffoldKoltResources`)

Runs automatically on the first `preBuild` of any module using `application`, `library-compose`, or `library-hilt-compose`.

Writes (once, never overwrites):
```
src/main/res/values/kolt_colors.xml         ← day-mode colour tokens
src/main/res/values-night/kolt_colors.xml   ← night-mode overrides
src/main/res/values/kolt_dimens.xml         ← spacing + type-scale tokens
```

**To customise the theme:** edit these XML files directly. They use the same resource names referenced by `compose-utils` (`Kolt.colors.*`, etc.) so your changes apply app-wide with no Kotlin changes.

**To re-scaffold** (e.g. after adding a new token): delete the file and rebuild.

### Steering docs (`scaffoldKoltDocs`)

Also runs on `preBuild`. Writes (once, never overwrites):
```
CLAUDE.md                               ← Claude Code entry (fill in project facts)
AGENTS.md                               ← Gemini/Antigravity entry (same)
docs/CODING_STANDARDS.md                ← binding rules (Clean + MVI + SOLID + stateless Compose)
docs/ARCHITECTURE.md                    ← layering, DI, offline-first, layer wiring
docs/TESTING.md                         ← test patterns, fakes, coverage targets
docs/KOLT.md                    ← this file
```

Fill in the `<placeholder>` sections in `CLAUDE.md` / `AGENTS.md` and commit them.

---

## 9. Dev workflow (UtilsLibs contributors)

This section is for developers working **on** the Kolt suite itself.

**Composite build:** the `libs/` modules apply convention plugins directly from source via `includeBuild("build-logic")`. No `publishToMavenLocal` round-trip is needed within this repo.

```bash
./gradlew build                                      # build all libs + verify plugins
./gradlew :build-logic:conventions:test              # run plugin TestKit tests (16 tests)
./gradlew :libs:compose-utils:assembleDebug          # build one lib
./gradlew publishToMavenLocal -PisRelease            # local publish for external testing
./gradlew bumpDevVersion                             # increment all DEV counters
./gradlew bumpPluginVersion                          # bump only plugin DEV counter
./gradlew scripts/publish-release.sh                # full Maven Central release
```

**Classloader architecture:** `build-logic/` has two subprojects — `:conventions` (all plugins, KGP/AGP as `compileOnly`) and `:publish` (vanniktech-only). The root `buildscript {}` in `build.gradle.kts` puts KGP/AGP/KSP/Compose on a shared ClassLoader so all lib modules avoid `KotlinNativeBundleBuildService` type conflicts. `SonatypeRepositoryBuildService` is registered once in `:publish`, gated by `isRelease`.

**Adding a new plugin:** create a `*ConventionPlugin.kt` class, register it in `build-logic/conventions/build.gradle.kts` under `gradlePlugin { plugins { create(...) } }`, add the alias in `gradle/koltlibs.versions.toml` or `kmplibs.versions.toml`.

**Updating steering docs:** edit files under `project-templates/`. They are packed into the JAR on every build and scaffolded into consumer projects automatically.

> See `UtilsLibs/AGENTS.md` for deeper architecture notes, plugin internals, and test strategy.
 
---

## 10. Migration Guide: `io.github.appspiriment.kolt` → `io.github.koltalabs.kolt`

If your project encounters:
```
Plugin [id: 'io.github.appspiriment.kolt.kmp.application'] was not found in any of the following sources:
```
or any other `io.github.appspiriment.*` resolution failure, update your project following the checklist below:

### 1. Update Plugin IDs in `build.gradle.kts`
Replace all legacy plugin declarations:

| Old Legacy Plugin ID | New Kolt Plugin ID | Version Catalog Accessor |
|---|---|---|
| `io.github.appspiriment.kolt.kmp.application` | `io.github.koltalabs.kolt.kmp.application` | `kmplibs.plugins.kmp.application` |
| `io.github.appspiriment.kolt.kmp.library` | `io.github.koltalabs.kolt.kmp.library` | `kmplibs.plugins.kmp.library` |
| `io.github.appspiriment.kolt.kmp.library-compose` | `io.github.koltalabs.kolt.kmp.library-compose` | `kmplibs.plugins.kmp.library.compose` |
| `io.github.appspiriment.kolt.kmp.library-koin` | `io.github.koltalabs.kolt.kmp.library-koin` | `kmplibs.plugins.kmp.library.koin` |
| `io.github.appspiriment.kolt.kmp.library-koin-compose` | `io.github.koltalabs.kolt.kmp.library-koin-compose` | `kmplibs.plugins.kmp.library.koin.compose` |
| `io.github.appspiriment.kolt.kmp.data` | `io.github.koltalabs.kolt.kmp.data` | `kmplibs.plugins.kmp.data` |
| `io.github.appspiriment.kolt.application` | `io.github.koltalabs.kolt.application` | `koltlibs.plugins.kolt.application` |
| `io.github.appspiriment.kolt.library` | `io.github.koltalabs.kolt.library` | `koltlibs.plugins.kolt.library` |
| `io.github.appspiriment.kolt.library-compose` | `io.github.koltalabs.kolt.library-compose` | `koltlibs.plugins.kolt.library.compose` |
| `io.github.appspiriment.kolt.library-hilt` | `io.github.koltalabs.kolt.library-hilt` | `koltlibs.plugins.kolt.library.hilt` |
| `io.github.appspiriment.kolt.library-hilt-compose` | `io.github.koltalabs.kolt.library-hilt-compose` | `koltlibs.plugins.kolt.library.hilt.compose` |
| `io.github.appspiriment.kolt.data` | `io.github.koltalabs.kolt.data` | `koltlibs.plugins.kolt.data` |
| `io.github.appspiriment.kolt.publish` | `io.github.koltalabs.kolt.publish` | — |

### 2. Update `settings.gradle.kts` Version Catalogs
```kotlin
dependencyResolutionManagement {
    versionCatalogs {
        create("koltlibs") {
            from("io.github.koltalabs.kolt:kolt-catalog:<version>")
        }
        create("kmplibs") {
            from("io.github.koltalabs.kolt:kmp-catalog:<version>")
        }
    }
}
```

### 3. Update BOM & Library Coordinates
```kotlin
// Old
implementation(platform("io.github.appspiriment.kolt:kolt-bom:<version>"))
implementation("io.github.appspiriment.kolt:utils")

// New
implementation(platform("io.github.koltalabs.kolt:kolt-bom:<version>"))
implementation("io.github.koltalabs.kolt:utils")
```

### 4. Package Names & UI Component Names
- Replace package imports: `io.github.appspiriment.kolt.*` $\to$ `io.github.koltalabs.kolt.*`
- Replace UI components: `Apps*` $\to$ `Kolt*` (e.g. `AppsButton` $\to$ `KoltButton`, `AppspirimentText` $\to$ `KoltText`, `AppspirimentTheme` $\to$ `KoltTheme`).

