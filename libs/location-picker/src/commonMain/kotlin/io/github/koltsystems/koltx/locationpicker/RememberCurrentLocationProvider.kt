package io.github.koltsystems.koltx.locationpicker

import androidx.compose.runtime.Composable
import io.github.koltsystems.koltx.location.CurrentLocationProvider

/**
 * Builds a [CurrentLocationProvider] with whatever platform handle it needs (an Android
 * `Context`, nothing on iOS/Desktop) so [LocationPickerScreen] doesn't need to know the
 * difference.
 */
@Composable
expect fun rememberCurrentLocationProvider(): CurrentLocationProvider
