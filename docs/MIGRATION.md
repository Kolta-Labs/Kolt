# Migration Guide: `io.github.appspiriment.kolt` → `io.github.koltalabs.kolt`

This guide explains how to migrate existing consumer and multiplatform projects from legacy `appspiriment` coordinates to **Kolt** (`io.github.koltalabs.kolt`).

---

## 1. Quick Resolution: "Plugin not found" Error

If your project fails to sync or build with:
```
Plugin [id: 'io.github.appspiriment.kolt.kmp.application'] was not found in any of the following sources:
```
(or any other `io.github.appspiriment.kolt.*` plugin id), it means the project is requesting the retired Gradle plugin coordinates.

### Fix
Open the corresponding `build.gradle.kts` file and update the plugin declaration:

```kotlin
// BEFORE (broken)
plugins {
    id("io.github.appspiriment.kolt.kmp.application")
}

// AFTER (fixed)
plugins {
    id("io.github.koltalabs.kolt.kmp.application")
    // or via version catalog: alias(kmplibs.plugins.kmp.application)
}
```

---

## 2. Full Convention Plugin ID Mapping

| Old Legacy Plugin ID | New Kolt Plugin ID | Catalog Accessor |
|---|---|---|
| `io.github.appspiriment.kolt.kmp.application` | `io.github.koltalabs.kolt.kmp.application` | `alias(kmplibs.plugins.kmp.application)` |
| `io.github.appspiriment.kolt.kmp.library` | `io.github.koltalabs.kolt.kmp.library` | `alias(kmplibs.plugins.kmp.library)` |
| `io.github.appspiriment.kolt.kmp.library-compose` | `io.github.koltalabs.kolt.kmp.library-compose` | `alias(kmplibs.plugins.kmp.library.compose)` |
| `io.github.appspiriment.kolt.kmp.library-koin` | `io.github.koltalabs.kolt.kmp.library-koin` | `alias(kmplibs.plugins.kmp.library.koin)` |
| `io.github.appspiriment.kolt.kmp.library-koin-compose` | `io.github.koltalabs.kolt.kmp.library-koin-compose` | `alias(kmplibs.plugins.kmp.library.koin.compose)` |
| `io.github.appspiriment.kolt.kmp.data` | `io.github.koltalabs.kolt.kmp.data` | `alias(kmplibs.plugins.kmp.data)` |
| `io.github.appspiriment.kolt.application` | `io.github.koltalabs.kolt.application` | `alias(koltlibs.plugins.kolt.application)` |
| `io.github.appspiriment.kolt.library` | `io.github.koltalabs.kolt.library` | `alias(koltlibs.plugins.kolt.library)` |
| `io.github.appspiriment.kolt.library-compose` | `io.github.koltalabs.kolt.library-compose` | `alias(koltlibs.plugins.kolt.library.compose)` |
| `io.github.appspiriment.kolt.library-hilt` | `io.github.koltalabs.kolt.library-hilt` | `alias(koltlibs.plugins.kolt.library.hilt)` |
| `io.github.appspiriment.kolt.library-hilt-compose` | `io.github.koltalabs.kolt.library-hilt-compose` | `alias(koltlibs.plugins.kolt.library.hilt.compose)` |
| `io.github.appspiriment.kolt.data` | `io.github.koltalabs.kolt.data` | `alias(koltlibs.plugins.kolt.data)` |
| `io.github.appspiriment.kolt.publish` | `io.github.koltalabs.kolt.publish` | — |

---

## 3. Version Catalog Configuration

In `settings.gradle.kts`:

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

> **Note:** For compatibility with older project files referencing `koltlibs.*`, you can also define:
> ```kotlin
> create("koltlibs") {
>     from("io.github.koltalabs.kolt:kolt-catalog:<version>")
> }
> ```

---

## 4. Bill of Materials (BOM) & Runtime Libraries

```kotlin
// BEFORE
dependencies {
    implementation(platform("io.github.appspiriment.kolt:kolt-bom:<version>"))
    implementation("io.github.appspiriment.kolt:utils")
    implementation("io.github.appspiriment.kolt:logutils")
    implementation("io.github.appspiriment.kolt:compose-kmp")
}

// AFTER
dependencies {
    implementation(platform("io.github.koltalabs.kolt:kolt-bom:<version>"))
    implementation("io.github.koltalabs.kolt:utils")
    implementation("io.github.koltalabs.kolt:logutils")
    implementation("io.github.koltalabs.kolt:compose-kmp")
}
```

---

## 5. Package Names & Imports

Update your Kotlin source imports across all source sets:

- `io.github.appspiriment.kolt.utils.*` → `io.github.koltalabs.kolt.utils.*`
- `io.github.appspiriment.kolt.logutils.*` → `io.github.koltalabs.kolt.logutils.*`
- `io.github.appspiriment.kolt.composekmp.*` → `io.github.koltalabs.kolt.composekmp.*`
- `io.github.appspiriment.kolt.composeutils.*` → `io.github.koltalabs.kolt.composeutils.*`
- `io.github.appspiriment.kolt.location.*` → `io.github.koltalabs.kolt.location.*`
- `io.github.appspiriment.kolt.locationpicker.*` → `io.github.koltalabs.kolt.locationpicker.*`
- `io.github.appspiriment.kolt.updateutils.*` → `io.github.koltalabs.kolt.updateutils.*`

---

## 6. UI Components & Theme Renames

All UI components in `compose-kmp` and `compose-utils` have been updated to the unified `Kolt*` brand prefix:

| Legacy Symbol | Kolt Symbol |
|---|---|
| `AppsButton` | `KoltButton` |
| `AppsPageScaffold` | `KoltPageScaffold` |
| `AppsTopBar` | `KoltTopBar` |
| `AppsCard` | `KoltCard` |
| `AppsAccordion` | `KoltAccordion` |
| `AppsBadge` | `KoltBadge` |
| `AppsTooltip` | `KoltTooltip` |
| `AppsStatusTag` | `KoltStatusTag` |
| `AppsSlider` | `KoltSlider` |
| `AppsRatingBar` | `KoltRatingBar` |
| `AppsDivider` | `KoltDivider` |
| `AppsBottomSheet` | `KoltBottomSheet` |
| `AppsDialog` | `KoltDialog` |
| `AppsBanner` | `KoltBanner` |
| `AppsImageText` | `KoltImageText` |
| `AppspirimentText` | `KoltText` |
| `AppspirimentTheme` | `KoltTheme` |
| `kolt { ... }` DSL | `koltx { ... }` DSL (legacy alias retained) |

---

## 7. Scaffolding Tasks

If your project was using Gradle tasks to scaffold theme resources or documentation:
- `./gradlew scaffoldKoltResources` (generates `kolt_colors.xml` and `kolt_dimens.xml`)
- `./gradlew scaffoldKoltDocs` (generates `docs/KOLT.md`, `docs/ARCHITECTURE.md`, etc.)

---

## 8. compose-kmp 0.2.1.dev-02: new type-scale slots (no migration needed)

Additive only. New slots, appended at the end of each constructor:

| sp | `Sizes` / `UiSizes` | `BaseTextStyles` | Android dimen |
|---|---|---|---|
| 17 | `fontSizeMediumLargeMid` | `textMediumLargeMid` | `font_size_medium_large_mid` |
| 22 | `fontSizeXLargeMid` | `textXLargeMid` | `font_size_xlarge_mid` |

No existing slot was renamed, reordered or re-valued, and the Material3 aliases
are unchanged. Optional cleanup: replace hand-written
`TextStyle(fontSize = 17.sp)` / `TextStyle(fontSize = 22.sp)` with
`Kolt.typography.body.mediumLargeMid` / `Kolt.typography.body.xLargeMid`.

Also in `0.2.1.dev-02`:

- **Title / body sets** — `Kolt.typography.title` and `Kolt.typography.body` each hold the whole size scale (`title.xLarge`, `body.xSmall`, …); weight stays an extension (`.bold`). `CompositionBaseProvider` now also fills `MaterialTheme.typography` from the Kolt scale, so Material components inside it switch from the M3 default typography to the Kolt font and sizes. If you want your own M3 typography, wrap your own `MaterialTheme(typography = …)` inside `CompositionBaseProvider`.
- **Deprecated aliases** — `Kolt.typography.bodyXXXSmall`, `bodyXSmall`, `bodySmall`, `bodyMedium`, `bodyMediumLarge`, `bodyLarge`, `labelSmall`, `labelMedium`, `labelLarge`, `titleSmall`, `titleMedium`, `titleLarge`, `headlineSmall`, `headlineMedium`, `displaySmall`. They still work. The IDE quick-fix swaps in the identical `body.*` style (no visual change).
- **Deprecated `text*` styles** — `Kolt.typography.textMinimum` … `textGiant`, `textMediumLargeMid`, `textXLargeMid`. Use `Kolt.typography.body.<size>` (or `title.<size>` for titles); the quick-fix gives the identical style. They're removed in 0.3.0; `BaseTextStyles(textX = …)` constructor args go with them.
- **iOS / wasmJs** — `Kolt.sizes`, `Kolt.uiSizes` and `Kolt.typography` now have the desktop default values instead of all-`Unspecified`. If you override `LocalSizes`/`LocalTypography`, nothing changes for you.
- **`UiSizes.fontSizeXBig`** (36 sp) added, appended last.

