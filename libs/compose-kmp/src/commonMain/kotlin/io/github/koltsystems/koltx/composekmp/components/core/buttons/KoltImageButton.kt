package io.github.koltsystems.koltx.composekmp.components.core.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.koltsystems.koltx.composekmp.components.core.buttons.types.ButtonStyle
import io.github.koltsystems.koltx.composekmp.theme.Kolt
import io.github.koltsystems.koltx.composekmp.theme.Kolt.sizes
import io.github.koltsystems.koltx.composekmp.wrappers.UiImage
import io.github.koltsystems.koltx.composekmp.wrappers.UiText

/**
 * A themed button with an optional leading or trailing icon, backed by [KoltIconTextButton].
 */
@Composable
fun KoltImageButton(
    icon: UiImage?,
    iconPosition: IconPosition = IconPosition.Start,
    text: UiText,
    modifier: Modifier = Modifier,
    iconPadding: Dp = Kolt.sizes.paddingSmall,
    textModifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonStyle: ButtonStyle = ButtonStyle.primary(),
    contentPadding: PaddingValues = PaddingValues(horizontal = sizes.paddingMedium, vertical = sizes.noPadding),
    onClick: () -> Unit
) {
    KoltIconTextButton(
        text = text,
        modifier = modifier,
        leadingIcon = icon.takeIf { iconPosition == IconPosition.Start },
        trailingIcon = icon.takeIf { iconPosition == IconPosition.End },
        iconPadding = iconPadding,
        textModifier = textModifier,
        enabled = enabled,
        buttonStyle = buttonStyle,
        contentPadding = contentPadding,
        onClick = onClick
    )
}

sealed interface IconPosition {
    data object Start : IconPosition
    data object End : IconPosition
}