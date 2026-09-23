package io.github.koltalabs.kolt.composekmp.components.core.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.koltalabs.kolt.composekmp.components.core.buttons.types.ButtonStyle
import io.github.koltalabs.kolt.composekmp.theme.Kolt.sizes
import io.github.koltalabs.kolt.composekmp.wrappers.UiImage
import io.github.koltalabs.kolt.composekmp.wrappers.UiText

/**
 * A themed outlined button.
 *
 * It uses [ButtonStyle.outlined()] by default.
 */
@Composable
fun KoltOutlinedButton(
    text: UiText,
    modifier: Modifier = Modifier,
    leadingIcon: UiImage? = null,
    trailingIcon: UiImage? = null,
    iconSize: Dp = sizes.iconSmall,
    iconPadding: Dp = sizes.paddingSmall,
    textModifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonStyle: ButtonStyle = ButtonStyle.outlined(),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = sizes.paddingMedium,
        vertical = sizes.noPadding,
    ),
    onClick: () -> Unit,
) {
    KoltIconTextButton(
        text = text,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        iconSize = iconSize,
        iconPadding = iconPadding,
        textModifier = textModifier,
        enabled = enabled,
        buttonStyle = buttonStyle,
        contentPadding = contentPadding,
        onClick = onClick,
    )
}
