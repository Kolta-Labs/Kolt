package io.github.koltalabs.kolt.composekmp.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal expect val platformTextStyleNoPadding: PlatformTextStyle?

object TextStyles {
    val NoPaddingStyle = TextStyle(
        platformStyle = platformTextStyleNoPadding,
    )
}

val TextStyle.noPadding: TextStyle
    get() = this.copy(
        platformStyle = platformTextStyleNoPadding
    )

/**
 * Switches this [TextStyle] to the Roboto font family (bundled with compose-utils).
 *
 * Useful for one-off overrides when the theme-level font is something else:
 * ```kotlin
 * KoltText(
 *     text = "Label".toUiText(),
 *     style = Kolt.typography.textSmall.roboto
 * )
 * ```
 */
val TextStyle.roboto: TextStyle
    get() = this.copy(fontFamily = GoogleFonts.robotoFamily)

/**
 * Switches this [TextStyle] to the Noto Sans font family (bundled with compose-utils).
 *
 * Useful for one-off overrides when you want Noto Sans on a specific text element
 * without changing the whole theme:
 * ```kotlin
 * KoltText(
 *     text = "മലയാളം".toUiText(),
 *     style = Kolt.typography.textMedium.noto
 * )
 * ```
 */
val TextStyle.noto: TextStyle
    get() = this.copy(fontFamily = GoogleFonts.notoFamily)


data class BaseTextStyles(
    val baseTextStyle: TextStyle = TextStyle.Default,
    @Deprecated("Use body.minimum (or title.minimum).", ReplaceWith("body.minimum"))
    val textMinimum: TextStyle = TextStyle.Default,
    @Deprecated("Use body.tiny (or title.tiny).", ReplaceWith("body.tiny"))
    val textTiny: TextStyle = TextStyle.Default,
    @Deprecated("Use body.xxxSmall (or title.xxxSmall).", ReplaceWith("body.xxxSmall"))
    val textXXXSmall: TextStyle = TextStyle.Default,
    @Deprecated("Use body.xxSmall (or title.xxSmall).", ReplaceWith("body.xxSmall"))
    val textXXSmall: TextStyle = TextStyle.Default,
    @Deprecated("Use body.xSmall (or title.xSmall).", ReplaceWith("body.xSmall"))
    val textXSmall: TextStyle = TextStyle.Default,
    @Deprecated("Use body.xSmallMedium (or title.xSmallMedium).", ReplaceWith("body.xSmallMedium"))
    val textXSmallMedium: TextStyle = TextStyle.Default,
    @Deprecated("Use body.small (or title.small).", ReplaceWith("body.small"))
    val textSmall: TextStyle = TextStyle.Default,
    @Deprecated("Use body.smallMedium (or title.smallMedium).", ReplaceWith("body.smallMedium"))
    val textSmallMedium: TextStyle = TextStyle.Default,
    @Deprecated("Use body.medium (or title.medium).", ReplaceWith("body.medium"))
    val textMedium: TextStyle = TextStyle.Default,
    @Deprecated("Use body.mediumMid (or title.mediumMid).", ReplaceWith("body.mediumMid"))
    val textMediumMid: TextStyle = TextStyle.Default,
    @Deprecated("Use body.mediumLarge (or title.mediumLarge).", ReplaceWith("body.mediumLarge"))
    val textMediumLarge: TextStyle = TextStyle.Default,
    @Deprecated("Use body.large (or title.large).", ReplaceWith("body.large"))
    val textLarge: TextStyle = TextStyle.Default,
    @Deprecated("Use body.xLarge (or title.xLarge).", ReplaceWith("body.xLarge"))
    val textXLarge: TextStyle = TextStyle.Default,
    @Deprecated("Use body.xxLarge (or title.xxLarge).", ReplaceWith("body.xxLarge"))
    val textXXLarge: TextStyle = TextStyle.Default,
    @Deprecated("Use body.xxxLarge (or title.xxxLarge).", ReplaceWith("body.xxxLarge"))
    val textXXXLarge: TextStyle = TextStyle.Default,
    @Deprecated("Use body.big (or title.big).", ReplaceWith("body.big"))
    val textBig: TextStyle = TextStyle.Default,
    @Deprecated("Use body.xBig (or title.xBig).", ReplaceWith("body.xBig"))
    val textXBig: TextStyle = TextStyle.Default,
    @Deprecated("Use body.huge (or title.huge).", ReplaceWith("body.huge"))
    val textHuge: TextStyle = TextStyle.Default,
    @Deprecated("Use body.giant (or title.giant).", ReplaceWith("body.giant"))
    val textGiant: TextStyle = TextStyle.Default,
    // Appended last (not in scale order) so existing positional args / componentN() stay stable.
    /** [Sizes.fontSizeMediumLargeMid] — between [textMediumLarge] and [textLarge]. */
    @Deprecated("Use body.mediumLargeMid (or title.mediumLargeMid).", ReplaceWith("body.mediumLargeMid"))
    val textMediumLargeMid: TextStyle = TextStyle.Default,
    /** [Sizes.fontSizeXLargeMid] — between [textXLarge] and [textXXLarge]. */
    @Deprecated("Use body.xLargeMid (or title.xLargeMid).", ReplaceWith("body.xLargeMid"))
    val textXLargeMid: TextStyle = TextStyle.Default,
) {
    // ── Title / body sets ─────────────────────────────────────────────────────
    // The full scale under each role, weight via the extensions below
    // (e.g. Kolt.typography.title.xLarge.bold). Both reference the text* styles above, so
    // no extra TextStyle copies; they diverge once titles get their own font. Body
    // properties, not constructor params, so equals()/componentN()/copy() are unchanged.

    /** Every step of the scale, for titles and headings. */
    val title: TextScale = TextScale(this)
    /** Every step of the scale, for running text. Same styles as the `text*` steps. */
    val body: TextScale = TextScale(this)

    // ── Material3-compatible aliases ──────────────────────────────────────────
    // These allow components designed with M3 typography names to work with the
    // Kolt theme without mechanical find-and-replace in each file.
    // The mappings follow rough visual equivalence (M3 body scale ≈ Kolt
    // text scale shifted by ~2 steps).

    /** M3 alias → [textXXSmall] */
    @Deprecated("Not an M3 role. Use body.xxSmall (identical).", ReplaceWith("body.xxSmall"))
    val bodyXXXSmall: TextStyle get() = body.xxSmall
    /** M3 alias → [textXSmall] */
    @Deprecated("Not an M3 role. Use body.xSmall (identical).", ReplaceWith("body.xSmall"))
    val bodyXSmall: TextStyle get() = body.xSmall
    /** M3 alias → [textSmall] */
    @Deprecated("Size differs from the M3 role. Use body.small (identical).", ReplaceWith("body.small"))
    val bodySmall: TextStyle get() = body.small
    /** M3 alias → [textMedium] */
    @Deprecated("Size differs from the M3 role. Use body.medium (identical).", ReplaceWith("body.medium"))
    val bodyMedium: TextStyle get() = body.medium
    /** M3 alias → [textMediumMid] */
    @Deprecated("Not an M3 role. Use body.mediumMid (identical).", ReplaceWith("body.mediumMid"))
    val bodyMediumLarge: TextStyle get() = body.mediumMid
    /** M3 alias → [textLarge] */
    @Deprecated("Size differs from the M3 role. Use body.large (identical).", ReplaceWith("body.large"))
    val bodyLarge: TextStyle get() = body.large
    /** M3 alias → [textSmall] */
    @Deprecated("Size differs from the M3 role. Use body.small (identical).", ReplaceWith("body.small"))
    val labelSmall: TextStyle get() = body.small
    /** M3 alias → [textSmallMedium] */
    @Deprecated("Size differs from the M3 role. Use body.smallMedium (identical).", ReplaceWith("body.smallMedium"))
    val labelMedium: TextStyle get() = body.smallMedium
    /** M3 alias → [textMedium] */
    @Deprecated("Size differs from the M3 role. Use body.medium (identical).", ReplaceWith("body.medium"))
    val labelLarge: TextStyle get() = body.medium
    /** M3 alias → [textMediumLarge] */
    @Deprecated("Size differs from the M3 role. Use body.mediumLarge (identical).", ReplaceWith("body.mediumLarge"))
    val titleSmall: TextStyle get() = body.mediumLarge
    /** M3 alias → [textLarge] */
    @Deprecated("Size differs from the M3 role. Use body.large (identical).", ReplaceWith("body.large"))
    val titleMedium: TextStyle get() = body.large
    /** M3 alias → [textXLarge] */
    @Deprecated("Size differs from the M3 role. Use body.xLarge (identical).", ReplaceWith("body.xLarge"))
    val titleLarge: TextStyle get() = body.xLarge
    /** M3 alias → [textXXLarge] */
    @Deprecated("Size differs from the M3 role. Use body.xxLarge (identical).", ReplaceWith("body.xxLarge"))
    val headlineSmall: TextStyle get() = body.xxLarge
    /** M3 alias → [textXXXLarge] */
    @Deprecated("Size differs from the M3 role. Use body.xxxLarge (identical).", ReplaceWith("body.xxxLarge"))
    val headlineMedium: TextStyle get() = body.xxxLarge
    /** M3 alias → [textBig] */
    @Deprecated("Size differs from the M3 role. Use body.big (identical).", ReplaceWith("body.big"))
    val displaySmall: TextStyle get() = body.big
}

// Not @Composable: pure mapping, no composition reads — keeps it unit-testable from commonTest.
internal fun createBaseTypography(baseSize: Sizes, fontFamily: FontFamily?): BaseTextStyles {
    val baseTextStyle = TextStyle.Default.copy(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        platformStyle = platformTextStyleNoPadding
    )
    return BaseTextStyles(
        baseTextStyle = baseTextStyle,
        textMinimum = baseTextStyle.copy(
            fontSize = baseSize.fontSizeMinimum
        ),
        textTiny = baseTextStyle.copy(
            fontSize = baseSize.fontSizeTiny
        ),
        textXXXSmall = baseTextStyle.copy(
            fontSize = baseSize.fontSizeXXXSmall
        ),
        textXXSmall = baseTextStyle.copy(
            fontSize = baseSize.fontSizeXXSmall
        ),
        textXSmall = baseTextStyle.copy(
            fontSize = baseSize.fontSizeXSmall
        ),
        textXSmallMedium = baseTextStyle.copy(
            fontSize = baseSize.fontSizeXSmallMedium
        ),
        textSmall = baseTextStyle.copy(
            fontSize = baseSize.fontSizeSmall
        ),
        textSmallMedium = baseTextStyle.copy(
            fontSize = baseSize.fontSizeSmallMedium
        ),
        textMedium = baseTextStyle.copy(
            fontSize = baseSize.fontSizeMedium
        ),
        textMediumMid = baseTextStyle.copy(
            fontSize = baseSize.fontSizeMediumMid
        ),
        textMediumLarge = baseTextStyle.copy(
            fontSize = baseSize.fontSizeMediumLarge
        ),
        textLarge = baseTextStyle.copy(
            fontSize = baseSize.fontSizeLarge
        ),
        textXLarge = baseTextStyle.copy(
            fontSize = baseSize.fontSizeXLarge
        ),
        textXXLarge = baseTextStyle.copy(
            fontSize = baseSize.fontSizeXXLarge
        ),
        textXXXLarge = baseTextStyle.copy(
            fontSize = baseSize.fontSizeXXXLarge
        ),
        textBig = baseTextStyle.copy(
            fontSize = baseSize.fontSizeBig
        ),
        textXBig = baseTextStyle.copy(
            fontSize = baseSize.fontSizeXBig
        ),
        textHuge = baseTextStyle.copy(
            fontSize = baseSize.fontSizeHuge
        ),
        textGiant = baseTextStyle.copy(
            fontSize = baseSize.fontSizeGiant
        ),
        textMediumLargeMid = baseTextStyle.copy(
            fontSize = baseSize.fontSizeMediumLargeMid
        ),
        textXLargeMid = baseTextStyle.copy(
            fontSize = baseSize.fontSizeXLargeMid
        ),

    )
}

/**
 * The whole Kolt size scale under one role. Properties reference [BaseTextStyles]' `text*`
 * styles directly (no copies). New steps go at the end, like the constructor params.
 */
@Suppress("DEPRECATION") // reads the text* storage; public API is this class
class TextScale internal constructor(steps: BaseTextStyles) {
    val minimum: TextStyle = steps.textMinimum
    val tiny: TextStyle = steps.textTiny
    val xxxSmall: TextStyle = steps.textXXXSmall
    val xxSmall: TextStyle = steps.textXXSmall
    val xSmall: TextStyle = steps.textXSmall
    val xSmallMedium: TextStyle = steps.textXSmallMedium
    val small: TextStyle = steps.textSmall
    val smallMedium: TextStyle = steps.textSmallMedium
    val medium: TextStyle = steps.textMedium
    val mediumMid: TextStyle = steps.textMediumMid
    val mediumLarge: TextStyle = steps.textMediumLarge
    val mediumLargeMid: TextStyle = steps.textMediumLargeMid
    val large: TextStyle = steps.textLarge
    val xLarge: TextStyle = steps.textXLarge
    val xLargeMid: TextStyle = steps.textXLargeMid
    val xxLarge: TextStyle = steps.textXXLarge
    val xxxLarge: TextStyle = steps.textXXXLarge
    val big: TextStyle = steps.textBig
    val xBig: TextStyle = steps.textXBig
    val huge: TextStyle = steps.textHuge
    val giant: TextStyle = steps.textGiant
}

/**
 * Material3 [Typography] for `MaterialTheme`, so Material components use the Kolt font and scale.
 * Internal: app code uses [BaseTextStyles]' steps / [TextScale] sets plus weight extensions.
 * Sizes follow the M3 baseline, except display, capped at the top of the Kolt scale.
 */
internal fun createTextRoles(steps: BaseTextStyles): Typography {
    val body = steps.body
    fun TextStyle.role(lineHeight: Int, letterSpacing: Double, weight: FontWeight = FontWeight.Normal) =
        copy(fontWeight = weight, lineHeight = lineHeight.sp, letterSpacing = letterSpacing.sp)
    return Typography(
        displayLarge = body.giant.role(56, -0.25),
        displayMedium = body.huge.role(48, 0.0),
        displaySmall = body.xBig.role(44, 0.0),
        headlineLarge = body.big.role(40, 0.0),
        headlineMedium = body.xxxLarge.role(36, 0.0),
        headlineSmall = body.xxLarge.role(32, 0.0),
        titleLarge = body.xLargeMid.role(28, 0.0),
        titleMedium = body.mediumLarge.role(24, 0.15, FontWeight.Medium),
        titleSmall = body.medium.role(20, 0.1, FontWeight.Medium),
        bodyLarge = body.mediumLarge.role(24, 0.5),
        bodyMedium = body.medium.role(20, 0.25),
        bodySmall = body.small.role(16, 0.4),
        labelLarge = body.medium.role(20, 0.1, FontWeight.Medium),
        labelMedium = body.small.role(16, 0.5, FontWeight.Medium),
        labelSmall = body.xSmallMedium.role(16, 0.5, FontWeight.Medium),
    )
}

val TextStyle.thin get() = this.copy(fontWeight = FontWeight.Thin)
val TextStyle.extraLight get() = this.copy(fontWeight = FontWeight.ExtraLight)
val TextStyle.light get() = this.copy(fontWeight = FontWeight.Light)
val TextStyle.normal get() = this.copy(fontWeight = FontWeight.Normal)
val TextStyle.medium get() = this.copy(fontWeight = FontWeight.Medium)
val TextStyle.semiBold get() = this.copy(fontWeight = FontWeight.SemiBold)
val TextStyle.bold get() = this.copy(fontWeight = FontWeight.Bold)
val TextStyle.extraBold get() = this.copy(fontWeight = FontWeight.ExtraBold)
val TextStyle.black get() = this.copy(fontWeight = FontWeight.Black)



val TextStyle.italic get() = this.copy(fontStyle = FontStyle.Italic)
val TextStyle.thinItalic get() = this.copy(fontWeight = FontWeight.Thin, fontStyle = FontStyle.Italic)
val TextStyle.extraLightItalic get() = this.copy(fontWeight = FontWeight.ExtraLight, fontStyle = FontStyle.Italic)
val TextStyle.lightItalic get() = this.copy(fontWeight = FontWeight.Light, fontStyle = FontStyle.Italic)
val TextStyle.mediumItalic get() = this.copy(fontWeight = FontWeight.Medium, fontStyle = FontStyle.Italic)
val TextStyle.semiBoldItalic get() = this.copy(fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic)
val TextStyle.boldItalic get() = this.copy(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
val TextStyle.extraBoldItalic get() = this.copy(fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Italic)
val TextStyle.blackItalic get() = this.copy(fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic)

val LocalTypography  by lazy { staticCompositionLocalOf { BaseTextStyles() } }

