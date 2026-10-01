# Kolt/libs — per-module detail

Read this only after [kolt-libs.md](kolt-libs.md#reuse-as-is-mandatory-when-present)'s
table has already pointed you at a specific module and you need more than the
one-line "why it's safe" — integrating `location-picker`, or touching its
permission/services flow. Skip this file otherwise; the overview table is
enough for "can I reuse X" in general.

## `location-picker` follows this steering set

On Android, `location-picker` follows this steering set:
`LocationPickerViewModel` extends the `MviViewModel` base from
[presentation-mvi.md](presentation-mvi.md); `State`/`Intent`/`Effect` live in
their own `LocationPickerMviContract.kt`; `LocationPickerScreenContent` is a
stateless renderer owned by `LocationPickerScreen`, which collects its
effects; its list field is `ImmutableList`; its UI reuses `compose-kmp`'s
theme/components instead of a hand-rolled theme; and `LocationPickerViewModel`
has test coverage (`LocationPickerViewModelTest` in `commonTest`, via a
`LocationPickerGateway` fake seam wrapping `libs/location`'s otherwise
unfakeable `CurrentLocationProvider`/`searchPlaces`/etc.). Safe to treat as a
reference example here, not just a black box.

## Its permission/services flow is self-contained

`rememberLocationAccessGate` gates "use current location" behind a permission
check, showing a `compose-kmp` `KoltBottomSheet` rationale (not a dialog),
falling back to an "open app settings" bottomsheet if permanently denied.
Once permission is granted, it resolves "location services off" via
`libs/location`'s own `rememberLocationSettingsResolver` — a Google Play
Services `SettingsClient` check that, in the common case, shows the system's
in-app "Turn on location" dialog directly (no bottomsheet, no navigation out
to Settings); only a device Play Services can't resolve automatically falls
back to a bottomsheet deep-linking to the device's location settings. Every
title/message/button label across all of this is configurable via
`LocationPickerConfig`. You do **not** need to request
`ACCESS_FINE_LOCATION`/`ACCESS_COARSE_LOCATION` yourself before using this
module — it's the one caller `libs/location`'s own doc defers that
responsibility to.

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
