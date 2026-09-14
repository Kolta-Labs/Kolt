# Theme Module

Self-contained — the shape below works with zero Kolt dependency, so if
`Kolt/libs` isn't in the workspace, implement the contract directly. **If it
is present, use it instead of writing this module from scratch** —
`compose-kmp/src/*/theme/` (`Theme.kt`, `Color.kt`, `Dimens.kt`, `AppFont.kt`,
`TextStyles.kt`) already implements this exact pattern, real KMP
(`commonMain` + `androidMain` + `iosMain` + `desktopMain` `expect`/`actual`),
and per [kolt-libs.md](kolt-libs.md) it's a vetted "reuse as-is" — not
optional once it's there.

## Shape

```kotlin
// :theme module, commonMain
val LocalColors = staticCompositionLocalOf<BaseColors> { error("No colors provided") }
val LocalTypography = staticCompositionLocalOf<BaseTextStyles> { error("No typography provided") }
val LocalSizes = staticCompositionLocalOf<Sizes> { error("No sizes provided") }

object AppTheme {
    val colors: BaseColors @Composable @ReadOnlyComposable get() = LocalColors.current
    val typography: BaseTextStyles @Composable @ReadOnlyComposable get() = LocalTypography.current
    val sizes: Sizes @Composable @ReadOnlyComposable get() = LocalSizes.current
}

@Composable
expect fun AppThemeProvider(
    isDarkTheme: Boolean? = null,   // null = follow system
    font: AppFont = AppFont.Default,
    content: @Composable () -> Unit,
)
```

Each platform's `actual AppThemeProvider` builds the platform `MaterialTheme`
(or equivalent) from the same tokens and provides the `CompositionLocal`s. If
using Kolt, its `Theme.android.kt` / `Theme.ios.kt` / `Theme.desktop.kt` is a
worked example (dark/light resolution, font loading per platform).

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
to user-facing text: use Compose Multiplatform resources
(`compose.resources`, generated `Res.string.x` from `composeResources/values/strings.xml`)
— not Android's `androidMain`-only `stringResource(R.string.x)`/`context.getString(...)`,
and not a literal string typed into a composable. This is `commonMain` from
the start (unlike the theme module, it needs no `expect`/`actual` — Compose
Multiplatform resources already generate one shared accessor), so there's no
excuse to special-case it per platform.

## Rules

- No `Color(0xFF...)`, `.sp`, `.dp` literals outside the `:theme` module.
  Screens reference `AppTheme.colors.primary`, `AppTheme.sizes.spacingM`, etc.
  This still applies to the two cases that most often slip through review:
  - **"On-X" contrast colors** — the text/icon color for content sitting on a
    colored fill (e.g. "black text on a gold chip, white in dark mode") is a
    token (`AppTheme.colors.onPrimary`, `onGold`, …), not an
    `if (isDark) Color.Black else Color.White` repeated per screen. The
    moment it appears in a second screen, extract it — the two copies drift
    (one screen quietly using a different black than another) far more
    easily than a single token would.
  - **Categorical/identity palettes** — a fixed color per enum case (calendar
    type, chart series, category tag) that's deliberately *invariant* across
    light/dark, because it identifies that item rather than styling the
    screen. These still belong physically inside the `:theme` module as a
    plain function/map (not a `PanchangikaColors`-style light/dark pair) —
    "it doesn't change with the theme" is not an excuse to hardcode it
    beside the screen that happens to use it first.
- No string literals for user-facing text in a composable — `Res.string.x`
  via Compose Multiplatform resources. (Log messages, internal tags, and
  test-only strings are fine as literals; this rule is about text a user
  sees.)
- Conditions (dark mode, dynamic color, locale-driven font) are parameters to
  the provider composable, not `if` branches scattered through screens (Kolt's
  `MalayalamCompositionBaseProvider` is a worked example of this if reusing it).
- One `AppThemeProvider` call, at the root of the single Activity/UIViewController
  (see [navigation.md](navigation.md)) — not per screen, not per journey.
- Adding a new design token (spacing, radius, elevation): add it to the
  `Sizes`/`Dimens` interface in `:theme`, not as a local `val` in a screen.
