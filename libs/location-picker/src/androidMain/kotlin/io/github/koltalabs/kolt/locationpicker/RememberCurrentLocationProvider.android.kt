package io.github.koltalabs.kolt.locationpicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.github.koltalabs.kolt.location.CurrentLocationProvider
import io.github.koltalabs.kolt.location.PlatformLocationContext

@Composable
actual fun rememberCurrentLocationProvider(): CurrentLocationProvider {
    val context = LocalContext.current
    return remember(context) { CurrentLocationProvider(PlatformLocationContext(context)) }
}
