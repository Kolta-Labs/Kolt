package io.github.koltsystems.koltx.composekmp.components.core.selectors

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.koltsystems.koltx.composekmp.theme.Kolt

/**
 * Ultra-sleek, aesthetic radio button with primary color gradient ring and
 * inner dot when active, featuring smooth spring physics animation.
 *
 * @param selected Whether this radio button is currently selected.
 * @param onClick Callback invoked when clicked. Pass null if click handling is on parent container.
 * @param modifier Modifier applied to the outer layout.
 * @param enabled Whether this control is interactive.
 * @param size Overall diameter of the radio button. Defaults to 18.dp.
 * @param shape Shape of the ring/dot. Defaults to [CircleShape]; pass a [RoundedCornerShape]
 * for a squircle/rounded-square radio style.
 * @param activeBrush Gradient brush applied to outer ring and inner dot when selected.
 * @param inactiveBorderColor Color of the outer ring when unselected.
 */
@Composable
fun KoltRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 18.dp,
    shape: Shape = CircleShape,
    activeBrush: Brush = Brush.horizontalGradient(
        listOf(Kolt.colors.primary, Kolt.colors.primary.copy(alpha = 0.82f))
    ),
    inactiveBorderColor: Color = Kolt.colors.dividerColor.copy(alpha = 0.8f),
) {
    val dotSize by animateDpAsState(
        targetValue = if (selected) size * 0.52f else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "KoltRadioDotSize"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(bounded = false, radius = size),
                        enabled = enabled,
                        onClick = onClick
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        // Outer ring: primary gradient border when selected, subtle hairline border when unselected
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    BorderStroke(
                        width = 1.5.dp,
                        brush = if (selected) activeBrush else SolidColor(inactiveBorderColor)
                    ),
                    shape = shape
                )
        )
        // Inner dot: primary gradient fill when selected
        if (selected && dotSize > 0.dp) {
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(shape)
                    .background(activeBrush)
            )
        }
    }
}
