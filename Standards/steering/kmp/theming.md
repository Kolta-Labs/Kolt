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

## Kolt title / body sets

If reusing Kolt, `Kolt.typography` has two complete copies of the size scale
below: `title` for titles/headings and `body` for running text. Pick the set,
then the size, then the weight with an extension:

```kotlin
Kolt.typography.title.xLarge.bold
Kolt.typography.body.medium
Kolt.typography.body.xSmall.light
```

Size names drop the `text` prefix (`xSmall`, `medium`, `xLargeMid`, `giant`, …).
Today both sets hold the same styles (same instances, no extra copies); they
diverge once titles get their own font. The flat `text*` steps
(`Kolt.typography.textXLarge`) are deprecated and removed in 0.3.0. Other roles
(display/headline/label) are on hold until per-role fonts are needed.
`CompositionBaseProvider` also fills `MaterialTheme.typography` from the Kolt
scale, so Material components use the Kolt font.

The old flat `Kolt.typography.bodyMedium`/`titleLarge`/… aliases are
deprecated: their sizes don't match Material3 (e.g. `bodyLarge` was 18 sp).
Each one's `ReplaceWith` points at the identical `body.*` step, so auto-fix
doesn't change how anything looks.

## Kolt type scale

If reusing Kolt, `Kolt.typography.body.x` / `title.x` map 1:1 to `Kolt.sizes.fontSizeX`
(sp values below are the Android `values/dimens.xml` / desktop defaults):

| sp | Slot | sp | Slot |
|---|---|---|---|
| 6 | `body.minimum` | 17 | `body.mediumLargeMid` (added 0.2.1-dev2) |
| 7 | `body.tiny` | 18 | `body.large` |
| 8 | `body.xxxSmall` | 20 | `body.xLarge` |
| 9 | `body.xxSmall` | 22 | `body.xLargeMid` (added 0.2.1-dev2) |
| 10 | `body.xSmall` | 24 | `body.xxLarge` |
| 11 | `body.xSmallMedium` | 28 | `body.xxxLarge` |
| 12 | `body.small` | 32 | `body.big` |
| 13 | `body.smallMedium` | 36 | `body.xBig` |
| 14 | `body.medium` | 40 | `body.huge` |
| 15 | `body.mediumMid` | 48 | `body.giant` |
| 16 | `body.mediumLarge` | | |

The two `…Mid` slots added in 0.2.1-dev2 are appended at the **end** of the
`Sizes`/`UiSizes`/`BaseTextStyles` constructors (not in scale order) so existing
positional args and `componentN()` stay stable. New slots follow the same
append-only rule.

## Kolt size scale

If reusing Kolt, every dimension comes from `Kolt.sizes` (dp; Android `values/dimens.xml` /
desktop defaults). Pick the family by *what* is being sized, then the step:

| Family | Use for | Steps (dp) |
|---|---|---|
| `padding*` | padding, `PaddingValues`, `spacedBy`, `Spacer` | Tiny 1, XXSmall 2, XSmall 4, XSmallPlus 6, Small 8, SmallMedium 12, Medium 16, Large 20, XLarge 24, XXLarge 32, XXXLarge 40, XXXXLarge 48, Giant 56 |
| `cornerRadius*` | shapes | Small 3, Medium 4, Normal 8, MediumLarge 12, Large 16, XLarge 20, XXLarge 24, XXXLarge 32 |
| `icon*` | icons, spinners | XSmall 8, Small 16, Medium 20, Standard 24, StandardLarge 28, Large 32, XLarge 40, XXLarge 48, XXXLarge 56, XXXXLarge 64, Big 80, Giant 128 |
| `size*` | other element sizes: button/row/bar heights, dots, boxes | XXXSmall 8, XXSmall 16, XSmall 24, Small 32, SmallMedium 36, Medium 40, MediumLarge 48, Large 56, XLarge 64, XXLarge 80, XXXLarge 96, Giant 128 |
| `image*` | avatars, thumbnails, illustrations | XXSmall 24, XSmall 32, Small 40, SmallMedium 48, SmallMediumPlus 56, Medium 64, MediumLarge 80, Large 96, XLarge 120, XXLarge 160, XXXLarge 200, Huge 240, Giant 320 |

`size*`, `image*` (incl. `imageSmallMediumPlus`) were added in 0.2.1-dev3, appended at the
**end** of `Sizes`/`UiSizes` — same append-only rule as the type scale. An app that provides
its own `LocalSizes` (a theme bridge) starts from `KoltDefaults.sizes()` and `copy()`s only
what differs; a bare `Sizes(...)` leaves every other field `Unspecified` (NaN at draw time).
A value off every scale (e.g. 14/18/22 dp icons, 0.75 dp hairlines) or pure layout geometry
(breakpoints, fixed panel widths) stays a named constant in the theme/dimens module, not a
token.

## Rules

- **Kolt tokens first.** Whenever Kolt is available (in the workspace, an `includeBuild`, a copied module, or an `io.github.koltalabs.kolt` dependency), always
  use its tokens for anything it covers: colors (`Kolt.colors.*`), padding,
  spacing, corner radius, icon/button sizes (`Kolt.sizes.*`), and text
  (`Kolt.typography.title.*` / `Kolt.typography.body.*`, weight via `.bold` etc.). Don't hand-write a
  literal or define a parallel `AppTheme` token that duplicates one. Only a
  value Kolt genuinely lacks gets an app token, added in the app's `:theme`
  module next to the Kolt ones. A raw value is allowed only when computed at
  runtime, with a one-line comment saying why.
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
- Text uses the `title` or `body` set (`Kolt.typography.title.xLarge.bold`,
  `Kolt.typography.body.medium`), with weight as an extension.
- **Replace old tokens with the current ones.** Whenever you touch code that uses
  a deprecated Kolt token, move it to the current token in the same change —
  don't leave it for later and don't add new uses. Follow the deprecation's
  `ReplaceWith`: `Kolt.typography.textXLarge` becomes `Kolt.typography.body.xLarge`
  (or `title.xLarge` if it styles a title or heading), and the flat
  `bodyMedium`/`titleLarge`/… aliases become the `body.*` step they name. Same for
  any other `@Deprecated` Kolt token (colors, sizes, components).
- Use a Kolt typography slot for any size on the scale; a raw
  `TextStyle(fontSize = …)` is only allowed for runtime-computed sizes
  (cell-proportional, user text-size multiplier, platform branch) and must
  carry a one-line comment saying why.
- If reusing Kolt: spacing uses `Kolt.sizes.padding*`, corners `cornerRadius*`, icons `icon*`,
  images/illustrations `image*` (24–320dp),
  and every other element dimension (button/row/bar heights, avatars, dots) `size*` (8–128dp).
  An app bridging its own `LocalSizes` starts from `KoltDefaults.sizes()` and `copy()`s only
  what differs.
- Adding a new design token (spacing, radius, elevation): add it to the
  `Sizes`/`Dimens` interface in `:theme`, not as a local `val` in a screen.
