package io.github.koltalabs.kolt.composekmp.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import io.github.koltalabs.kolt.composekmp.wrappers.UiDimen


data class Sizes(
    val appBarSize: Dp = Dp.Unspecified,

    val iconXXSmall: Dp = Dp.Unspecified,
    val iconXSmall: Dp = Dp.Unspecified,
    val iconSmall: Dp = Dp.Unspecified,
    val iconMedium: Dp = Dp.Unspecified,
    val iconStandard: Dp = Dp.Unspecified,
    val iconStandardLarge: Dp = Dp.Unspecified,
    val iconLarge: Dp = Dp.Unspecified,
    val iconXLarge: Dp = Dp.Unspecified,
    val iconXXLarge: Dp = Dp.Unspecified,
    val iconXXXLarge: Dp = Dp.Unspecified,
    val iconXXXXLarge: Dp = Dp.Unspecified,
    val iconBig: Dp = Dp.Unspecified,
    val iconGiant: Dp = Dp.Unspecified,
    /** Large illustration size for empty-state / hero screens. */
    val illustrationLarge: Dp = Dp.Unspecified,

    val paddingGiant: Dp = Dp.Unspecified,
    val paddingXXXXLarge: Dp = Dp.Unspecified,
    val paddingXXXLarge: Dp = Dp.Unspecified,
    val paddingXXLarge: Dp = Dp.Unspecified,
    val paddingXLarge: Dp = Dp.Unspecified,
    val paddingLarge: Dp = Dp.Unspecified,
    val paddingMedium: Dp = Dp.Unspecified,
    val paddingSmallMedium: Dp = Dp.Unspecified,
    val paddingSmall: Dp = Dp.Unspecified,
    val paddingXSmallPlus: Dp = Dp.Unspecified,
    val paddingXSmall: Dp = Dp.Unspecified,
    val paddingXXSmall: Dp = Dp.Unspecified,
    val paddingTiny: Dp = Dp.Unspecified,
    val noPadding: Dp = Dp.Unspecified,

    val cornerRadiusSmall: Dp = Dp.Unspecified,
    val cornerRadiusMedium: Dp = Dp.Unspecified,
    val cornerRadiusNormal: Dp = Dp.Unspecified,
    val cornerRadiusMediumLarge: Dp = Dp.Unspecified,
    val cornerRadiusLarge: Dp = Dp.Unspecified,
    val cornerRadiusXLarge: Dp = Dp.Unspecified,
    val cornerRadiusXXLarge: Dp = Dp.Unspecified,
    val cornerRadiusXXXLarge: Dp = Dp.Unspecified,

    val actionButtonSize: Dp = Dp.Unspecified,
    val floatingButtonSizeSmall: Dp = Dp.Unspecified,
    val floatingButtonSize: Dp = Dp.Unspecified,
    val floatingButtonSizeLarge: Dp = Dp.Unspecified,

    val fontSizeMinimum: TextUnit = TextUnit.Unspecified,
    val fontSizeTiny: TextUnit = TextUnit.Unspecified,
    val fontSizeXXXSmall: TextUnit = TextUnit.Unspecified,
    val fontSizeXXSmall: TextUnit = TextUnit.Unspecified,
    val fontSizeXSmall: TextUnit = TextUnit.Unspecified,
    val fontSizeXSmallMedium: TextUnit = TextUnit.Unspecified,
    val fontSizeSmall: TextUnit = TextUnit.Unspecified,
    val fontSizeSmallMedium: TextUnit = TextUnit.Unspecified,
    val fontSizeMedium: TextUnit = TextUnit.Unspecified,
    val fontSizeMediumMid: TextUnit = TextUnit.Unspecified,
    val fontSizeMediumLarge: TextUnit = TextUnit.Unspecified,
    val fontSizeLarge: TextUnit = TextUnit.Unspecified,
    val fontSizeXLarge: TextUnit = TextUnit.Unspecified,
    val fontSizeXXLarge: TextUnit = TextUnit.Unspecified,
    val fontSizeXXXLarge: TextUnit = TextUnit.Unspecified,
    val fontSizeBig: TextUnit = TextUnit.Unspecified,
    val fontSizeXBig: TextUnit = TextUnit.Unspecified,
    val fontSizeHuge: TextUnit = TextUnit.Unspecified,
    val fontSizeGiant: TextUnit = TextUnit.Unspecified,
    // Appended last (not in scale order) so existing positional args / componentN() stay stable.
    /** 17 sp — between [fontSizeMediumLarge] (16) and [fontSizeLarge] (18). */
    val fontSizeMediumLargeMid: TextUnit = TextUnit.Unspecified,
    /** 22 sp — between [fontSizeXLarge] (20) and [fontSizeXXLarge] (24). */
    val fontSizeXLargeMid: TextUnit = TextUnit.Unspecified,
    // General element sizes (button/row/bar heights, avatars, dots, boxes) — 8..128dp, separate
    // from the icon* scale so non-icon dimensions don't borrow icon names. Appended last.
    val sizeXXXSmall: Dp = Dp.Unspecified,
    val sizeXXSmall: Dp = Dp.Unspecified,
    val sizeXSmall: Dp = Dp.Unspecified,
    val sizeSmall: Dp = Dp.Unspecified,
    val sizeSmallMedium: Dp = Dp.Unspecified,
    val sizeMedium: Dp = Dp.Unspecified,
    val sizeMediumLarge: Dp = Dp.Unspecified,
    val sizeLarge: Dp = Dp.Unspecified,
    val sizeXLarge: Dp = Dp.Unspecified,
    val sizeXXLarge: Dp = Dp.Unspecified,
    val sizeXXXLarge: Dp = Dp.Unspecified,
    val sizeGiant: Dp = Dp.Unspecified,
    // Image/illustration sizes (avatars, thumbnails, empty-state and hero illustrations) —
    // 24..320dp. Appended last.
    val imageXXSmall: Dp = Dp.Unspecified,
    val imageXSmall: Dp = Dp.Unspecified,
    val imageSmall: Dp = Dp.Unspecified,
    val imageSmallMedium: Dp = Dp.Unspecified,
    val imageMedium: Dp = Dp.Unspecified,
    val imageMediumLarge: Dp = Dp.Unspecified,
    val imageLarge: Dp = Dp.Unspecified,
    val imageXLarge: Dp = Dp.Unspecified,
    val imageXXLarge: Dp = Dp.Unspecified,
    val imageXXXLarge: Dp = Dp.Unspecified,
    val imageHuge: Dp = Dp.Unspecified,
    val imageGiant: Dp = Dp.Unspecified,
    /** 56 dp (Material list-item thumbnail) — between imageSmallMedium 48 and imageMedium 64; appended last. */
    val imageSmallMediumPlus: Dp = Dp.Unspecified,
)


data class UiSizes(
    val appBarSize: UiDimen = UiDimen.DynamicDp.Unspecified,

    val iconXSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val iconSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val iconMedium: UiDimen = UiDimen.DynamicDp.Unspecified,
    val iconStandard: UiDimen = UiDimen.DynamicDp.Unspecified,
    val iconStandardLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val iconLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val iconXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val iconXXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val iconXXXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val iconXXXXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val iconBig: UiDimen = UiDimen.DynamicDp.Unspecified,
    val iconGiant: UiDimen = UiDimen.DynamicDp.Unspecified,

    val paddingGiant: UiDimen = UiDimen.DynamicDp.Unspecified,
    val paddingXXXXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val paddingXXXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val paddingXXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val paddingXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val paddingLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val paddingMedium: UiDimen = UiDimen.DynamicDp.Unspecified,
    val paddingSmallMedium: UiDimen = UiDimen.DynamicDp.Unspecified,
    val paddingSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val paddingXSmallPlus: UiDimen = UiDimen.DynamicDp.Unspecified,
    val paddingXSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val paddingXXSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val paddingTiny: UiDimen = UiDimen.DynamicDp.Unspecified,
    val noPadding: UiDimen = UiDimen.DynamicDp.Unspecified,

    val cornerRadiusSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val cornerRadiusMedium: UiDimen = UiDimen.DynamicDp.Unspecified,
    val cornerRadiusNormal: UiDimen = UiDimen.DynamicDp.Unspecified,
    val cornerRadiusMediumLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val cornerRadiusLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val cornerRadiusXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val cornerRadiusXXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val cornerRadiusXXXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,

    val actionButtonSize: UiDimen = UiDimen.DynamicDp.Unspecified,
    val floatingButtonSizeSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val floatingButtonSize: UiDimen = UiDimen.DynamicDp.Unspecified,
    val floatingButtonSizeLarge: UiDimen = UiDimen.DynamicDp.Unspecified,

    val fontSizeMinimum: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeTiny: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeXXXSmall: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeXXSmall: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeXSmall: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeXSmallMedium: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeSmall: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeSmallMedium: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeMedium: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeMediumMid: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeMediumLarge: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeLarge: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeXLarge: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeXXLarge: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeXXXLarge: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeBig: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeHuge: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    val fontSizeGiant: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    // Appended last (not in scale order) so existing positional args / componentN() stay stable.
    /** 17 sp — between [fontSizeMediumLarge] (16) and [fontSizeLarge] (18). */
    val fontSizeMediumLargeMid: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    /** 22 sp — between [fontSizeXLarge] (20) and [fontSizeXXLarge] (24). */
    val fontSizeXLargeMid: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    /** 36 sp — mirrors [Sizes.fontSizeXBig]; appended last, not in scale order. */
    val fontSizeXBig: UiDimen = UiDimen.DynamicTextUnit.Unspecified,
    // General element sizes (button/row/bar heights, avatars, dots, boxes) — 8..128dp, separate
    // from the icon* scale so non-icon dimensions don't borrow icon names. Appended last.
    val sizeXXXSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val sizeXXSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val sizeXSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val sizeSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val sizeSmallMedium: UiDimen = UiDimen.DynamicDp.Unspecified,
    val sizeMedium: UiDimen = UiDimen.DynamicDp.Unspecified,
    val sizeMediumLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val sizeLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val sizeXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val sizeXXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val sizeXXXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val sizeGiant: UiDimen = UiDimen.DynamicDp.Unspecified,
    // Image/illustration sizes (avatars, thumbnails, empty-state and hero illustrations) —
    // 24..320dp. Appended last.
    val imageXXSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val imageXSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val imageSmall: UiDimen = UiDimen.DynamicDp.Unspecified,
    val imageSmallMedium: UiDimen = UiDimen.DynamicDp.Unspecified,
    val imageMedium: UiDimen = UiDimen.DynamicDp.Unspecified,
    val imageMediumLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val imageLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val imageXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val imageXXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val imageXXXLarge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val imageHuge: UiDimen = UiDimen.DynamicDp.Unspecified,
    val imageGiant: UiDimen = UiDimen.DynamicDp.Unspecified,
    /** 56 dp — mirrors [Sizes.imageSmallMediumPlus]. */
    val imageSmallMediumPlus: UiDimen = UiDimen.DynamicDp.Unspecified,
)

@Composable
internal expect fun createSizes(): Sizes

@Composable
internal expect fun createUiSizes(): UiSizes

/**
 * Kolt's own per-platform default scale — Android reads `dimens.xml` (so screen-width
 * qualifiers apply), other platforms use the shared desktop defaults. For apps that provide
 * their own [LocalSizes] (e.g. a theme bridge onto an app palette): start from these and
 * `copy()` only the fields that differ, instead of rebuilding [Sizes] and leaving the rest
 * `Unspecified`.
 */
object KoltDefaults {
    @Composable
    fun sizes(): Sizes = createSizes()

    @Composable
    fun uiSizes(): UiSizes = createUiSizes()
}

val LocalSizes by lazy { staticCompositionLocalOf { Sizes() } }
val LocalUiSizes by lazy { staticCompositionLocalOf { UiSizes() } }
