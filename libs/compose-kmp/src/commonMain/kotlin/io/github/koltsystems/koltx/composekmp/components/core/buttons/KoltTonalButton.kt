package io.github.koltsystems.koltx.composekmp.components.core.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.koltsystems.koltx.composekmp.components.core.buttons.types.ButtonStyle
import io.github.koltsystems.koltx.composekmp.theme.Kolt.sizes
import io.github.koltsystems.koltx.composekmp.wrappers.UiImage
import io.github.koltsystems.koltx.composekmp.wrappers.UiText

/**
 * A themed tonal button with low-to-medium emphasis.
 *
 * It uses [ButtonStyle.tonal()] by default.
 */
@Composable
fun KoltTonalButton(
    text: UiText,
    modifier: Modifier = Modifier,
    leadingIcon: UiImage? = null,
    trailingIcon: UiImage? = null,
    textModifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonStyle: ButtonStyle = ButtonStyle.tonal(),
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
        textModifier = textModifier,
        enabled = enabled,
        buttonStyle = buttonStyle,
        contentPadding = contentPadding,
        onClick = onClick,
    )
}
