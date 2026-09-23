package io.github.koltalabs.kolt.locationpicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.koltalabs.kolt.location.CurrentLocationProvider
import io.github.koltalabs.kolt.location.PlatformLocationContext

@Composable
actual fun rememberCurrentLocationProvider(): CurrentLocationProvider =
    remember { CurrentLocationProvider(PlatformLocationContext()) }
