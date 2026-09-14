package io.github.koltsystems.koltx.composekmp.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.koltsystems.koltx.composekmp.wrappers.toUiDimen

// Desktop has no density-qualified resources (no dimens.xml equivalent), so these mirror the
// values in androidMain/res/values/dimens.xml as plain dp/sp literals — same numbers, no
// resource lookup. Previously this returned bare Sizes()/UiSizes(), whose fields all default to
// Dp.Unspecified/TextUnit.Unspecified; any KoltLibs widget that reads one of those un-overridden
// (e.g. KoltTextButton's default contentPadding) fed NaN into Modifier.padding and crashed with
// "Padding must be non-negative" the first time it was exercised on desktop.

@Composable
actual fun createSizes(): Sizes = Sizes(
    appBarSize = 56.dp,

    iconXSmall = 8.dp,
    iconSmall = 16.dp,
    iconMedium = 20.dp,
    iconStandard = 24.dp,
    iconStandardLarge = 28.dp,
    iconLarge = 32.dp,
    iconXLarge = 40.dp,
    iconXXLarge = 48.dp,
    iconXXXLarge = 56.dp,
    iconXXXXLarge = 64.dp,
    iconBig = 80.dp,
    iconGiant = 128.dp,

    paddingGiant = 56.dp,
    paddingXXXXLarge = 48.dp,
    paddingXXXLarge = 40.dp,
    paddingXXLarge = 32.dp,
    paddingXLarge = 24.dp,
    paddingLarge = 20.dp,
    paddingMedium = 16.dp,
    paddingSmallMedium = 12.dp,
    paddingSmall = 8.dp,
    paddingXSmallPlus = 6.dp,
    paddingXSmall = 4.dp,
    paddingXXSmall = 2.dp,
    paddingTiny = 1.dp,
    noPadding = 0.dp,

    cornerRadiusSmall = 3.dp,
    cornerRadiusMedium = 4.dp,
    cornerRadiusNormal = 8.dp,
    cornerRadiusMediumLarge = 12.dp,
    cornerRadiusLarge = 16.dp,
    cornerRadiusXLarge = 20.dp,
    cornerRadiusXXLarge = 24.dp,
    cornerRadiusXXXLarge = 32.dp,

    actionButtonSize = 48.dp,
    floatingButtonSizeSmall = 40.dp,
    floatingButtonSize = 48.dp,
    floatingButtonSizeLarge = 56.dp,

    fontSizeMinimum = 6.sp,
    fontSizeTiny = 7.sp,
    fontSizeXXXSmall = 8.sp,
    fontSizeXXSmall = 9.sp,
    fontSizeXSmall = 10.sp,
    fontSizeXSmallMedium = 11.sp,
    fontSizeSmall = 12.sp,
    fontSizeSmallMedium = 13.sp,
    fontSizeMedium = 14.sp,
    fontSizeMediumMid = 15.sp,
    fontSizeMediumLarge = 16.sp,
    fontSizeLarge = 18.sp,
    fontSizeXLarge = 20.sp,
    fontSizeXXLarge = 24.sp,
    fontSizeXXXLarge = 28.sp,
    fontSizeBig = 32.sp,
    fontSizeXBig = 36.sp,
    fontSizeHuge = 40.sp,
    fontSizeGiant = 48.sp,
)

@Composable
actual fun createUiSizes(): UiSizes = UiSizes(
    appBarSize = 56.dp.toUiDimen(),

    iconXSmall = 8.dp.toUiDimen(),
    iconSmall = 16.dp.toUiDimen(),
    iconMedium = 20.dp.toUiDimen(),
    iconStandard = 24.dp.toUiDimen(),
    iconStandardLarge = 28.dp.toUiDimen(),
    iconLarge = 32.dp.toUiDimen(),
    iconXLarge = 40.dp.toUiDimen(),
    iconXXLarge = 48.dp.toUiDimen(),
    iconXXXLarge = 56.dp.toUiDimen(),
    iconXXXXLarge = 64.dp.toUiDimen(),
    iconBig = 80.dp.toUiDimen(),
    iconGiant = 128.dp.toUiDimen(),

    paddingGiant = 56.dp.toUiDimen(),
    paddingXXXXLarge = 48.dp.toUiDimen(),
    paddingXXXLarge = 40.dp.toUiDimen(),
    paddingXXLarge = 32.dp.toUiDimen(),
    paddingXLarge = 24.dp.toUiDimen(),
    paddingLarge = 20.dp.toUiDimen(),
    paddingMedium = 16.dp.toUiDimen(),
    paddingSmallMedium = 12.dp.toUiDimen(),
    paddingSmall = 8.dp.toUiDimen(),
    paddingXSmallPlus = 6.dp.toUiDimen(),
    paddingXSmall = 4.dp.toUiDimen(),
    paddingXXSmall = 2.dp.toUiDimen(),
    paddingTiny = 1.dp.toUiDimen(),
    noPadding = 0.dp.toUiDimen(),

    cornerRadiusSmall = 3.dp.toUiDimen(),
    cornerRadiusMedium = 4.dp.toUiDimen(),
    cornerRadiusNormal = 8.dp.toUiDimen(),
    cornerRadiusMediumLarge = 12.dp.toUiDimen(),
    cornerRadiusLarge = 16.dp.toUiDimen(),
    cornerRadiusXLarge = 20.dp.toUiDimen(),
    cornerRadiusXXLarge = 24.dp.toUiDimen(),
    cornerRadiusXXXLarge = 32.dp.toUiDimen(),

    actionButtonSize = 48.dp.toUiDimen(),
    floatingButtonSizeSmall = 40.dp.toUiDimen(),
    floatingButtonSize = 48.dp.toUiDimen(),
    floatingButtonSizeLarge = 56.dp.toUiDimen(),

    fontSizeMinimum = 6.sp.toUiDimen(),
    fontSizeTiny = 7.sp.toUiDimen(),
    fontSizeXXXSmall = 8.sp.toUiDimen(),
    fontSizeXXSmall = 9.sp.toUiDimen(),
    fontSizeXSmall = 10.sp.toUiDimen(),
    fontSizeXSmallMedium = 11.sp.toUiDimen(),
    fontSizeSmall = 12.sp.toUiDimen(),
    fontSizeSmallMedium = 13.sp.toUiDimen(),
    fontSizeMedium = 14.sp.toUiDimen(),
    fontSizeMediumMid = 15.sp.toUiDimen(),
    fontSizeMediumLarge = 16.sp.toUiDimen(),
    fontSizeLarge = 18.sp.toUiDimen(),
    fontSizeXLarge = 20.sp.toUiDimen(),
    fontSizeXXLarge = 24.sp.toUiDimen(),
    fontSizeXXXLarge = 28.sp.toUiDimen(),
    fontSizeBig = 32.sp.toUiDimen(),
    fontSizeHuge = 40.sp.toUiDimen(),
    fontSizeGiant = 48.sp.toUiDimen(),
)
