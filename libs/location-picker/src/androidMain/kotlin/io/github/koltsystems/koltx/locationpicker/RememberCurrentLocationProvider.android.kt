package io.github.koltsystems.koltx.locationpicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.github.koltsystems.koltx.location.CurrentLocationProvider
import io.github.koltsystems.koltx.location.PlatformLocationContext

@Composable
actual fun rememberCurrentLocationProvider(): CurrentLocationProvider {
    val context = LocalContext.current
    return remember(context) { CurrentLocationProvider(PlatformLocationContext(context)) }
}
