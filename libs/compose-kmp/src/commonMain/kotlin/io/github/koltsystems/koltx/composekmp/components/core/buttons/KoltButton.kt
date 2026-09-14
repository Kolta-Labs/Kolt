package io.github.koltsystems.koltx.composekmp.components.core.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.koltsystems.koltx.composekmp.components.core.buttons.types.ButtonStyle
import io.github.koltsystems.koltx.composekmp.wrappers.UiText


@Composable
fun KoltButton(
    text: UiText,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonStyle: ButtonStyle = ButtonStyle.primary(),
    onClick: () -> Unit
) {
    KoltImageButton(
        icon = null,
        text = text,
        modifier = modifier,
        textModifier = textModifier,
        enabled = enabled,
        buttonStyle = buttonStyle,
        onClick = onClick
    )
}