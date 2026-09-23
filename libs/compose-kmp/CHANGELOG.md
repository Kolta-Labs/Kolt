# Changelog

All notable changes to `compose-kmp` (`io.github.koltalabs.kolt:compose-kmp`) are documented
here, newest first. Format loosely follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [0.2.1.dev-02] - 2026-09-23
- Added type-scale slots `fontSizeMediumLargeMid` / `textMediumLargeMid` (17 sp, between `MediumLarge` 16 and `Large` 18) and `fontSizeXLargeMid` / `textXLargeMid` (22 sp, between `XLarge` 20 and `XXLarge` 24) to `Sizes`, `UiSizes` and `BaseTextStyles`, with Android dimens `font_size_medium_large_mid` / `font_size_xlarge_mid` and desktop actuals.
- Additive only: new params are appended at the end of each constructor, so named callers and existing `componentN()` are unaffected. The JVM constructor signatures of `Sizes`, `UiSizes` and `BaseTextStyles` changed — recompile consumers that call them positionally from Java or prebuilt binaries.
- Added `Kolt.textRoles`: the 15 Material3 text roles built from the Kolt scale, with weight, line height and letter spacing. `CompositionBaseProvider` now also wraps content in `MaterialTheme(typography = …)` with them (color scheme and shapes still come from the enclosing `MaterialTheme`). **Visual change:** Material components (`Text` default style, `Button`, `TextField`, `TopAppBar`, …) inside the provider now use the Kolt font and these roles instead of the M3 defaults.
- Deprecated the `BaseTextStyles` M3-style aliases (`bodyXXXSmall`…`displaySmall`); their sizes don't match M3. `ReplaceWith` points at the identical `text*` step. Internal usages migrated with no visual change.
- `UiSizes` gained `fontSizeXBig` (36 sp), appended last, to match `Sizes`.
- iOS and wasmJs now get the same default sizes as desktop (moved to a shared `defaultSizes()`/`defaultUiSizes()`), instead of all-`Unspecified`. **Visual change** on those platforms: `Kolt.typography`/`Kolt.sizes` now have real values, where text previously fell back to the platform default size.
- Android: replaced deprecated `Resources.getColor(Int)` with `Context.getColor` in `UiColor.getColor`, and `DisplayMetrics.scaledDensity` with `density * configuration.fontScale` in `UiDimen` (same values).

## [0.2.1.dev-01] - 2026-09-21
- Added `KoltCheckBox` (gradient fill, configurable `shape`/corner rounding) to `components/core/selectors`.
- Added `KoltRadioButtonGroup` (single-selection list of `KoltRadioButton`s) to `components/core/selectors`.
- `KoltRadioButton` gained a `shape` param (defaults to `CircleShape`) for rounded-square styling.
- Resolved Material 3 deprecations: updated `TopAppBarColors`, unified tooltip position providers, and updated `menuAnchor` parameters.
- Parameterized `KoltDropdownCore` with `<T>`, eliminating unchecked cast.
- Suppressed `LocalClipboardManager` deprecation with CMP-7624 rationale.

## [0.2.1.dev-00] - 2026-08-08
- Changelog tracking starts here — see the git log for prior history.
