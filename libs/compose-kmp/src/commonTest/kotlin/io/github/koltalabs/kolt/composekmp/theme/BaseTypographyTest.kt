package io.github.koltalabs.kolt.composekmp.theme

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class BaseTypographyTest {

    @Test
    fun midSlotsMapFromSizesAndExistingSlotsUnchanged() {
        val typography = createBaseTypography(
            baseSize = Sizes(
                fontSizeMediumMid = 15.sp,
                fontSizeMediumLarge = 16.sp,
                fontSizeXLarge = 20.sp,
                fontSizeMediumLargeMid = 17.sp,
                fontSizeXLargeMid = 22.sp,
            ),
            fontFamily = null,
        )

        assertEquals(17.sp, typography.body.mediumLargeMid.fontSize)
        assertEquals(22.sp, typography.body.xLargeMid.fontSize)
        assertEquals(15.sp, typography.body.mediumMid.fontSize)
        assertEquals(16.sp, typography.body.mediumLarge.fontSize)
        assertEquals(20.sp, typography.body.xLarge.fontSize)
    }

    @Test
    fun titleAndBodySetsReuseTheSteps() {
        val typography = createBaseTypography(defaultSizes(), fontFamily = null)

        assertEquals(48.sp, typography.title.giant.fontSize)
        assertEquals(22.sp, typography.title.xLargeMid.fontSize)
        assertEquals(10.sp, typography.body.xSmall.fontSize)
        // Same instance as the step: no copy per set.
        assertSame(typography.body.xLarge, typography.title.xLarge)
        assertSame(typography.body.xLarge, typography.body.xLarge)
        // Weight stays an extension.
        assertEquals(FontWeight.Bold, typography.title.xLarge.bold.fontWeight)
    }

    @Test
    fun materialTypographyUsesKoltScale() {
        val roles = createTextRoles(createBaseTypography(defaultSizes(), fontFamily = null))

        assertEquals(22.sp, roles.titleLarge.fontSize)
        assertEquals(14.sp, roles.bodyMedium.fontSize)
        assertEquals(FontWeight.Medium, roles.titleMedium.fontWeight)
    }
}
