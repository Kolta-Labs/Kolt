# Kolt/libs — per-module detail

Read this only after [kolt-libs.md](kolt-libs.md#reuse-as-is-mandatory-when-present)'s
table has already pointed you at a specific module and you need more than the
one-line "why it's safe" — integrating `location-picker`, touching Android
permission/services flows, or verifying the `wasmJs` gap doesn't affect a
target you're building for. Skip this file otherwise; the overview table is
enough for "can I reuse X" in general.

## `location-picker` follows this steering set

On Android/iOS/Desktop: `LocationPickerViewModel` extends the `MviViewModel`
base from [presentation-mvi.md](presentation-mvi.md); `State`/`Intent`/`Effect`
live in their own `LocationPickerMviContract.kt`; `LocationPickerScreenContent`
is a stateless renderer (`LocationPickerScreen` is the composable that owns
the ViewModel and collects its effects, the Route-equivalent for a module
with no Navigation-3 back stack); `State.placeResults` is `ImmutableList`;
those three platforms' UI reuses the `compose-kmp` theme/components instead
of a hand-rolled theme; and `LocationPickerViewModel` has `commonTest`
coverage (`LocationPickerViewModelTest`, via a `LocationPickerGateway` fake
seam — `libs/location`'s `CurrentLocationProvider`/`searchPlaces`/etc. aren't
fakeable on their own).

## Android permission/services handling is self-contained

`location-picker` gates its own "use current location" trigger through
`rememberLocationAccessGate` (Android `actual` only, no expect/actual
counterpart needed): permission rationale and "permanently denied → open app
settings" are each a `compose-kmp` `KoltBottomSheet` (not a dialog), with
every title/message/button label configurable via `LocationPickerConfig`'s
`locationPermissionRationale*`/`locationPermissionSettings*` fields. A
consuming app does **not** need to request
`ACCESS_FINE_LOCATION`/`ACCESS_COARSE_LOCATION` itself before using this
module on Android — that's the one case where `libs/location`'s own doc
("Android: assumes the caller has already requested the permission") doesn't
apply, because `location-picker` is that caller. iOS/Desktop don't need this:
iOS's `CurrentLocationProvider` requests its own `CLLocationManager`
authorization, and Desktop has no permission/services concept.

Once permission is granted, "location services off" is resolved via
`libs/location`'s own `rememberLocationSettingsResolver` — a Google Play
Services `SettingsClient` check that, in the common resolvable case, shows the
system's in-app "Turn on location" dialog directly (no bottomsheet of
`location-picker`'s own, no navigation out to the Settings app). This
Composable lives in `libs/location` itself (not `location-picker`), Android
`androidMain` only, alongside `isLocationServicesEnabled` — the two are
complementary: `isLocationServicesEnabled` is a synchronous, no-`Activity`
check (used in `CurrentLocationProvider`'s own suspend fast-fail path);
`rememberLocationSettingsResolver` is the Compose/`Activity`-launcher-backed
"and let the user turn it on in-app" step. `LocationPickerConfig`'s
`locationServicesDisabled*` fields still exist, but now back only the rare
fallback bottomsheet for a device Play Services can't resolve automatically —
not the common path.

## The one documented `wasmJs` gap

`compose-kmp` has no `wasmJs` target, and a `commonMain` dependency must
resolve for every enabled target — so `LocationPickerScreenContent`'s Web
`actual` is a separately-maintained, plain-Material3 duplicate of the
android/ios/desktop one, not the same `compose-kmp`-based code shared via
`commonMain`. This is exactly the kind of per-module platform gap this doc
already tracks elsewhere (`location`'s own `wasmJs` Geolocation binding) —
not a reason to avoid the module, just don't expect Web's rendering code to
be the same file as the other three. Its `commonTest` suite also doesn't run
under `wasmJsTest` (a Skiko/webpack test-bundling gap in the browser test
runner, unrelated to the tests themselves — they pass on Android and
Desktop, which compile and run the exact same `commonTest` source).

## Don't import the `compose-utils` stub files

`KoltBanner`/`KoltBottomSheet`/`KoltSnackbar`/`DialogButtonStyle`/
`MessageDialog`/`ColorUtils` still exist as filenames in `compose-utils`, but
they're 10-line backward-compat `typealias` stubs pointing at the
`compose-kmp` versions (each file says so in a header comment: "Moved to
compose-kmp. This file is a backward-compatibility stub."). Import from
`compose-kmp` directly.

## Dead code — don't copy the pattern

`compose-utils/.../base/ViewModelDelegate.kt` is an `internal class` meant to
de-duplicate `UiStateEventsViewModel` and `UiStateEventsAndroidViewModel` (its
own doc comment says so). Nothing in the repo constructs it — grep confirms
zero usages. `UiStateEventsAndroidViewModel` reimplements the same
state/effect-channel logic by hand instead of delegating to it. Net effect:
two divergent copies of the same logic plus one unused third copy. Don't
carry this inconsistency into a new project; the `MviViewModel` in
[presentation-mvi.md](presentation-mvi.md) is the single version to use.
