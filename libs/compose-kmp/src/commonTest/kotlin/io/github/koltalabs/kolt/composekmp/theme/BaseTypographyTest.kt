package io.github.koltalabs.kolt.composekmp.theme

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlin.test.Test
import kotlin.test.assertEquals

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

        assertEquals(17.sp, typography.textMediumLargeMid.fontSize)
        assertEquals(22.sp, typography.textXLargeMid.fontSize)
        assertEquals(15.sp, typography.textMediumMid.fontSize)
        assertEquals(16.sp, typography.textMediumLarge.fontSize)
        assertEquals(20.sp, typography.textXLarge.fontSize)
    }

    @Test
    fun textRolesUseScaleStepsWithRoleWeight() {
        val roles = createTextRoles(createBaseTypography(defaultSizes(), fontFamily = null))

        assertEquals(22.sp, roles.titleLarge.fontSize)
        assertEquals(14.sp, roles.bodyMedium.fontSize)
        assertEquals(20.sp, roles.bodyMedium.lineHeight)
        assertEquals(FontWeight.Medium, roles.titleMedium.fontWeight)
        assertEquals(FontWeight.Normal, roles.bodyLarge.fontWeight)
    }
}
