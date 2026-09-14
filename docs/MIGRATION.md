# Migration Guide: `io.github.appspiriment.kolt` → `io.github.koltsystems.koltx`

This guide explains how to migrate existing consumer and multiplatform projects from legacy `appspiriment` coordinates to **KoltX** (`io.github.koltsystems.koltx`).

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
    id("io.github.koltsystems.koltx.kmp.application")
    // or via version catalog: alias(kmplibs.plugins.kmp.application)
}
```

---

## 2. Full Convention Plugin ID Mapping

| Old Legacy Plugin ID | New KoltX Plugin ID | Catalog Accessor |
|---|---|---|
| `io.github.appspiriment.kolt.kmp.application` | `io.github.koltsystems.koltx.kmp.application` | `alias(kmplibs.plugins.kmp.application)` |
| `io.github.appspiriment.kolt.kmp.library` | `io.github.koltsystems.koltx.kmp.library` | `alias(kmplibs.plugins.kmp.library)` |
| `io.github.appspiriment.kolt.kmp.library-compose` | `io.github.koltsystems.koltx.kmp.library-compose` | `alias(kmplibs.plugins.kmp.library.compose)` |
| `io.github.appspiriment.kolt.kmp.library-koin` | `io.github.koltsystems.koltx.kmp.library-koin` | `alias(kmplibs.plugins.kmp.library.koin)` |
| `io.github.appspiriment.kolt.kmp.library-koin-compose` | `io.github.koltsystems.koltx.kmp.library-koin-compose` | `alias(kmplibs.plugins.kmp.library.koin.compose)` |
| `io.github.appspiriment.kolt.kmp.data` | `io.github.koltsystems.koltx.kmp.data` | `alias(kmplibs.plugins.kmp.data)` |
| `io.github.appspiriment.kolt.application` | `io.github.koltsystems.koltx.application` | `alias(koltxlibs.plugins.koltx.application)` |
| `io.github.appspiriment.kolt.library` | `io.github.koltsystems.koltx.library` | `alias(koltxlibs.plugins.koltx.library)` |
| `io.github.appspiriment.kolt.library-compose` | `io.github.koltsystems.koltx.library-compose` | `alias(koltxlibs.plugins.koltx.library.compose)` |
| `io.github.appspiriment.kolt.library-hilt` | `io.github.koltsystems.koltx.library-hilt` | `alias(koltxlibs.plugins.koltx.library.hilt)` |
| `io.github.appspiriment.kolt.library-hilt-compose` | `io.github.koltsystems.koltx.library-hilt-compose` | `alias(koltxlibs.plugins.koltx.library.hilt.compose)` |
| `io.github.appspiriment.kolt.data` | `io.github.koltsystems.koltx.data` | `alias(koltxlibs.plugins.koltx.data)` |
| `io.github.appspiriment.kolt.publish` | `io.github.koltsystems.koltx.publish` | — |

---

## 3. Version Catalog Configuration

In `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    versionCatalogs {
        create("koltxlibs") {
            from("io.github.koltsystems.koltx:koltx-catalog:<version>")
        }
        create("kmplibs") {
            from("io.github.koltsystems.koltx:kmp-catalog:<version>")
        }
    }
}
```

> **Note:** For compatibility with older project files referencing `koltlibs.*`, you can also define:
> ```kotlin
> create("koltlibs") {
>     from("io.github.koltsystems.koltx:koltx-catalog:<version>")
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
    implementation(platform("io.github.koltsystems.koltx:koltx-bom:<version>"))
    implementation("io.github.koltsystems.koltx:utils")
    implementation("io.github.koltsystems.koltx:logutils")
    implementation("io.github.koltsystems.koltx:compose-kmp")
}
```

---

## 5. Package Names & Imports

Update your Kotlin source imports across all source sets:

- `io.github.appspiriment.kolt.utils.*` → `io.github.koltsystems.koltx.utils.*`
- `io.github.appspiriment.kolt.logutils.*` → `io.github.koltsystems.koltx.logutils.*`
- `io.github.appspiriment.kolt.composekmp.*` → `io.github.koltsystems.koltx.composekmp.*`
- `io.github.appspiriment.kolt.composeutils.*` → `io.github.koltsystems.koltx.composeutils.*`
- `io.github.appspiriment.kolt.location.*` → `io.github.koltsystems.koltx.location.*`
- `io.github.appspiriment.kolt.locationpicker.*` → `io.github.koltsystems.koltx.locationpicker.*`
- `io.github.appspiriment.kolt.updateutils.*` → `io.github.koltsystems.koltx.updateutils.*`

---

## 6. UI Components & Theme Renames

All UI components in `compose-kmp` and `compose-utils` have been updated to the unified `Kolt*` brand prefix:

| Legacy Symbol | KoltX Symbol |
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
| `AppspirimentTheme` | `KoltXTheme` |
| `kolt { ... }` DSL | `koltx { ... }` DSL (legacy alias retained) |

---

## 7. Scaffolding Tasks

If your project was using Gradle tasks to scaffold theme resources or documentation:
- `./gradlew scaffoldKoltResources` (generates `koltx_colors.xml` and `koltx_dimens.xml`)
- `./gradlew scaffoldKoltDocs` (generates `docs/KOLTX.md`, `docs/ARCHITECTURE.md`, etc.)
