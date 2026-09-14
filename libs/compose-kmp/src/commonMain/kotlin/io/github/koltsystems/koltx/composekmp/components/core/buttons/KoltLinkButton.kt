package io.github.koltsystems.koltx.composekmp.components.core.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import io.github.koltsystems.koltx.composekmp.components.core.image.KoltIcon
import io.github.koltsystems.koltx.composekmp.components.core.text.KoltText
import io.github.koltsystems.koltx.composekmp.theme.Kolt
import io.github.koltsystems.koltx.composekmp.wrappers.UiImage
import io.github.koltsystems.koltx.composekmp.wrappers.UiText
import io.github.koltsystems.koltx.composekmp.wrappers.toUiText

/**
 * A hyperlink-style inline action: underlined text in [color] with optional leading / trailing
 * icons. No background surface, no button container, no ripple box — just the text (and icons)
 * with a standard click area.
 *
 * ### When to use
 * - "Learn more", "View details", "Forgot password?" — inline navigational nudges.
 * - Anywhere a full [KoltButton] or [KoltTextButton] would feel too heavy (e.g. inside a card,
 *   at the end of a sentence, below a form field).
 *
 * Prefer [KoltTextButton] when you need a proper Material touch-target rectangle with ripple.
 *
 * ### Icon placement
 * Provide [leadingIcon] and/or [trailingIcon] independently — unlike [KoltImageButton] both can
 * be shown at the same time.
 *
 * ```kotlin
 * KoltLinkButton(
 *     text = "Open in browser".toUiText(),
 *     trailingIcon = uiImageResource(R.drawable.ic_open_external),
 *     onClick = { /* navigate */ },
 * )
 * ```
 *
 * @param text          Link label. Rendered with [TextDecoration.Underline] by default.
 * @param modifier      Applied to the outer [Row] touch-target.
 * @param leadingIcon   Optional icon rendered before [text].
 * @param trailingIcon  Optional icon rendered after [text].
 * @param enabled       When `false`, the link is non-interactive and rendered at 38 % opacity.
 * @param color         Text and icon tint. Defaults to [Kolt.colors.primary].
 * @param disabledColor Tint when [enabled] is `false`. Defaults to [color] at 38 % alpha.
 * @param textStyle     Text style for the label. Pass `textStyle.copy(textDecoration = null)` to
 *                      remove the underline.
 * @param iconSize      Width and height applied to both [leadingIcon] and [trailingIcon].
 * @param iconSpacing   Gap between the icon(s) and the label text.
 * @param onClick       Called on tap. Not invoked when [enabled] is `false`.
 */
@Composable
fun KoltLinkButton(
    text: UiText,
    modifier: Modifier = Modifier,
    leadingIcon: UiImage? = null,
    trailingIcon: UiImage? = null,
    enabled: Boolean = true,
    color: Color = Kolt.colors.primary,
    disabledColor: Color = color.copy(alpha = 0.38f),
    textStyle: TextStyle = Kolt.typography.bodyMedium.copy(
        textDecoration = TextDecoration.Underline,
    ),
    iconSize: Dp = Kolt.sizes.iconSmall,
    iconSpacing: Dp = Kolt.sizes.paddingXSmall,
    onClick: () -> Unit,
) {
    val effectiveColor = if (enabled) color else disabledColor

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(iconSpacing),
        modifier = modifier
            .alpha(if (enabled) 1f else 0.38f)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(vertical = Kolt.sizes.paddingXSmall),
    ) {
        leadingIcon?.let {
            KoltIcon(
                icon = it,
                modifier = Modifier.size(iconSize),
                tint = effectiveColor,
            )
        }

        KoltText(
            text = text,
            style = textStyle,
            color = effectiveColor,
        )

        trailingIcon?.let {
            KoltIcon(
                icon = it,
                modifier = Modifier.size(iconSize),
                tint = effectiveColor,
            )
        }
    }
}

