package io.github.koltsystems.koltx.composekmp.components.core.selectors

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.koltsystems.koltx.composekmp.theme.Kolt

/**
 * Ultra-sleek, aesthetic checkbox with primary color gradient fill and
 * animated checkmark, featuring configurable corner rounding.
 *
 * @param checked Whether this checkbox is currently checked.
 * @param onCheckedChange Callback invoked when clicked. Pass null if click handling is on parent container.
 * @param modifier Modifier applied to the outer layout.
 * @param enabled Whether this control is interactive.
 * @param size Overall width/height of the checkbox. Defaults to 18.dp.
 * @param shape Shape of the checkbox box, e.g. [RoundedCornerShape] for configurable rounded corners.
 * @param activeBrush Gradient brush applied to the fill and border when checked.
 * @param inactiveBorderColor Color of the border when unchecked.
 * @param checkmarkColor Color of the checkmark icon when checked.
 */
@Composable
fun KoltCheckBox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 18.dp,
    shape: Shape = RoundedCornerShape(Kolt.sizes.cornerRadiusSmall),
    activeBrush: Brush = Brush.horizontalGradient(
        listOf(Kolt.colors.primary, Kolt.colors.primary.copy(alpha = 0.82f))
    ),
    inactiveBorderColor: Color = Kolt.colors.dividerColor.copy(alpha = 0.8f),
    checkmarkColor: Color = Color.White,
) {
    val checkScale by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "KoltCheckBoxCheckScale"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .then(
                if (onCheckedChange != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(bounded = false, radius = size),
                        enabled = enabled,
                        onClick = { onCheckedChange(!checked) }
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        // Fill layer: gradient fill when checked, transparent when unchecked
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = if (checked) activeBrush else SolidColor(Color.Transparent), shape = shape)
                .border(
                    BorderStroke(
                        width = 1.5.dp,
                        brush = if (checked) activeBrush else SolidColor(inactiveBorderColor)
                    ),
                    shape = shape
                )
        )
        // Checkmark, animated in on check
        if (checkScale > 0f) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = checkmarkColor,
                modifier = Modifier
                    .size(size * 0.7f)
                    .scale(checkScale)
            )
        }
    }
}
