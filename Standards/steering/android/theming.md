# Theme Module

Self-contained — the shape below works with zero Kolt dependency, so if
`Kolt/libs` isn't in the workspace, implement the contract directly. **If it
is present, use it instead of writing this module from scratch** —
`compose-kmp/src/*/theme/` (`Theme.kt`, `Color.kt`, `Dimens.kt`, `AppFont.kt`,
`TextStyles.kt`) already implements this exact pattern, and per
[kolt-libs.md](kolt-libs.md) it's a vetted "reuse as-is" — not optional once
it's there. It's KMP-authored (real `expect`/`actual` across `commonMain`/
`androidMain`/`iosMain`/`desktopMain`), which is irrelevant baggage for an
Android-only app but not a problem — it compiles and runs as a normal
dependency, you just never touch the non-`androidMain` source sets.

## Shape

No `expect`/`actual` needed here — this is a single-platform module, so a
plain `object`/interface set is enough. `CompositionLocal` is still the
right mechanism (testable overrides, no global mutable state, scoped
overrides for one screen without touching every call site):

```kotlin
// :theme module
val LocalColors = staticCompositionLocalOf<BaseColors> { error("No colors provided") }
val LocalTypography = staticCompositionLocalOf<BaseTextStyles> { error("No typography provided") }
val LocalSizes = staticCompositionLocalOf<Sizes> { error("No sizes provided") }

object AppTheme {
    val colors: BaseColors @Composable @ReadOnlyComposable get() = LocalColors.current
    val typography: BaseTextStyles @Composable @ReadOnlyComposable get() = LocalTypography.current
    val sizes: Sizes @Composable @ReadOnlyComposable get() = LocalSizes.current
}

@Composable
fun AppThemeProvider(
    isDarkTheme: Boolean? = null,   // null = follow system
    content: @Composable () -> Unit,
) {
    val darkTheme = isDarkTheme ?: isSystemInDarkTheme()
    val colors = if (darkTheme) DarkColors else LightColors
    CompositionLocalProvider(LocalColors provides colors, LocalTypography provides AppTypography, LocalSizes provides AppSizes) {
        MaterialTheme(colorScheme = colors.toMaterialColorScheme(), typography = AppTypography.toMaterialTypography()) {
            content()
        }
    }
}
```

If using Kolt's version instead, `Theme.android.kt` is the worked example
(dark/light resolution, font loading) — just note it's the `actual` for one
platform among several; you only need what's in that file, not the
`iosMain`/`desktopMain` siblings.

## Material 3 Expressive

Current Material3 (`developer.android.com/develop/ui/compose/designsystems/material3`,
updated June 2026) adds `MaterialExpressiveTheme` and a `motionScheme`
parameter (`MotionScheme.expressive()` / `.standard()`) alongside the usual
`colorScheme`/`typography`/`shapes`. Treat `motionScheme` as another token
this module owns — expose it through the same provider (`AppThemeProvider`)
and `CompositionLocal` set as colors/typography, don't hardcode
`MaterialExpressiveTheme` vs `MaterialTheme` per screen.

## Strings / i18n

Same "no hardcoded token outside its module" shape as colors/dimens, applied
to user-facing text: use Android's standard resource system —
`stringResource(R.string.x)` in Compose, backed by `res/values/strings.xml`
(and `res/values-<locale>/strings.xml` per locale) — not a literal string
typed into a composable. (Compose Multiplatform resources
(`compose.resources`) are the KMP steering set's equivalent; don't reach for
that dependency here, it's solving a cross-platform problem this app doesn't
have.)

## Kolt text roles

Screens pick a **role**, not a size. If reusing Kolt, `Kolt.textRoles` (also
provided as `MaterialTheme.typography`, so Material components match) holds the
15 Material3 roles, built from the scale below with weight, line height and
letter spacing:

| Role | sp | Step | Weight |
|---|---|---|---|
| `displayLarge` / `Medium` / `Small` | 48 / 40 / 36 | `Giant` / `Huge` / `XBig` | Normal |
| `headlineLarge` / `Medium` / `Small` | 32 / 28 / 24 | `Big` / `XXXLarge` / `XXLarge` | Normal |
| `titleLarge` | 22 | `XLargeMid` | Normal |
| `titleMedium` / `Small` | 16 / 14 | `MediumLarge` / `Medium` | Medium |
| `bodyLarge` / `Medium` / `Small` | 16 / 14 / 12 | `MediumLarge` / `Medium` / `Small` | Normal |
| `labelLarge` / `Medium` / `Small` | 14 / 12 / 11 | `Medium` / `Small` / `XSmallMedium` | Medium |

Display is capped at the top of the Kolt scale (M3 baseline is 57/45/36).
The old `Kolt.typography.bodyX`/`titleX`/… aliases are deprecated: their sizes
don't match M3 (e.g. `bodyLarge` was 18 sp). Each one's `ReplaceWith` points at
the identical `text*` step, so auto-fix doesn't change how anything looks.

## Kolt type scale

If reusing Kolt, `Kolt.typography.textX` maps 1:1 to `Kolt.sizes.fontSizeX`
(sp values below are the Android `values/dimens.xml` / desktop defaults):

| sp | Slot | sp | Slot |
|---|---|---|---|
| 6 | `textMinimum` | 17 | `textMediumLargeMid` (added 0.2.1-dev2) |
| 7 | `textTiny` | 18 | `textLarge` |
| 8 | `textXXXSmall` | 20 | `textXLarge` |
| 9 | `textXXSmall` | 22 | `textXLargeMid` (added 0.2.1-dev2) |
| 10 | `textXSmall` | 24 | `textXXLarge` |
| 11 | `textXSmallMedium` | 28 | `textXXXLarge` |
| 12 | `textSmall` | 32 | `textBig` |
| 13 | `textSmallMedium` | 36 | `textXBig` |
| 14 | `textMedium` | 40 | `textHuge` |
| 15 | `textMediumMid` | 48 | `textGiant` |
| 16 | `textMediumLarge` | | |

The two `…Mid` slots added in 0.2.1-dev2 are appended at the **end** of the
`Sizes`/`UiSizes`/`BaseTextStyles` constructors (not in scale order) so existing
positional args and `componentN()` stay stable. New slots follow the same
append-only rule.

## Rules

- No `Color(0xFF...)`, `.sp`, `.dp` literals outside the `:theme` module.
  Screens reference `AppTheme.colors.primary`, `AppTheme.sizes.spacingM`, etc.
- No string literals for user-facing text in a composable — `stringResource(R.string.x)`.
  (Log messages, internal tags, and test-only strings are fine as literals;
  this rule is about text a user sees.)
- Conditions (dark mode, dynamic color, locale-driven font) are parameters to
  the provider composable, not `if` branches scattered through screens.
- One `AppThemeProvider` call, at the root of the single Activity (see
  [navigation.md](navigation.md)) — not per screen, not per journey.
- Text uses a role (`Kolt.textRoles.bodyMedium`, …). Use a `Kolt.typography.text*`
  step only when no role fits (e.g. 17 sp, dense numeric grids).
- Use a Kolt typography slot for any size on the scale; a raw
  `TextStyle(fontSize = …)` is only allowed for runtime-computed sizes
  (cell-proportional, user text-size multiplier, platform branch) and must
  carry a one-line comment saying why.
- Adding a new design token (spacing, radius, elevation): add it to the
  `Sizes`/`Dimens` interface in `:theme`, not as a local `val` in a screen.
