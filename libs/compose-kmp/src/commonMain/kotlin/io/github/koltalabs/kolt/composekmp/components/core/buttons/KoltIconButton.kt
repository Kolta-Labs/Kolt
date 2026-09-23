package io.github.koltalabs.kolt.composekmp.components.core.buttons

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.koltalabs.kolt.composekmp.components.core.image.KoltIcon
import io.github.koltalabs.kolt.composekmp.theme.Kolt
import io.github.koltalabs.kolt.composekmp.wrappers.UiImage
import io.github.koltalabs.kolt.composekmp.wrappers.asColor

@Composable
fun KoltIconButton(
    icon: UiImage,
    modifier: Modifier = Modifier.size(Kolt.sizes.actionButtonSize),
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    iconModifier: Modifier = Modifier,
    iconHeight: Dp? = Kolt.sizes.actionButtonSize,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    ) {
        val resolvedTint = if (enabled) {
            icon.tint?.asColor() ?: LocalContentColor.current
        } else {
            LocalContentColor.current
        }
        KoltIcon(
            icon = icon,
            iconHeight = iconHeight,
            modifier = iconModifier,
            tint = resolvedTint
        )
    }
}
