package io.github.koltalabs.kolt.composekmp.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.koltalabs.kolt.composekmp.wrappers.toUiDimen
import kotlin.test.Test
import kotlin.test.assertEquals

// Desktop, iOS and wasmJs all build their theme from these defaults.
class DefaultDimensTest {

    @Test
    fun defaultsAreSpecified() {
        val sizes = defaultSizes()
        assertEquals(14.sp, sizes.fontSizeMedium)
        assertEquals(36.sp, sizes.fontSizeXBig)
        assertEquals(16.dp, sizes.paddingMedium)

        val uiSizes = defaultUiSizes()
        assertEquals(14.sp.toUiDimen(), uiSizes.fontSizeMedium)
        assertEquals(36.sp.toUiDimen(), uiSizes.fontSizeXBig)
    }
}
