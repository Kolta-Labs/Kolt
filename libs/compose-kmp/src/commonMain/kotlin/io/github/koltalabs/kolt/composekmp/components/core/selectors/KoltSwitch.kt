package io.github.koltalabs.kolt.composekmp.components.core.selectors

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.koltalabs.kolt.composekmp.theme.Kolt

/**
 * Ultra-sleek, aesthetic toggle switch with primary gradient track styling and
 * smooth spring physics transitions.
 *
 * @param checked Whether the switch is currently checked.
 * @param onCheckedChange Callback invoked when the user toggles the switch.
 * @param modifier Modifier applied to the outer layout.
 * @param enabled Whether the switch is interactive.
 * @param width Total width of the switch track. Defaults to 36.dp.
 * @param height Total height of the switch track. Defaults to 20.dp.
 * @param thumbSize Diameter of the switch thumb circle. Defaults to 16.dp.
 * @param activeTrackBrush Gradient brush applied to the track when checked.
 * @param inactiveTrackColor Color of the track when unchecked.
 * @param thumbColor Color of the switch thumb.
 * @param borderColor Subtle border color for the track.
 */
@Composable
fun KoltSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    width: Dp = 36.dp,
    height: Dp = 20.dp,
    thumbSize: Dp = 16.dp,
    activeTrackBrush: Brush = Brush.horizontalGradient(
        listOf(Kolt.colors.primary, Kolt.colors.primary.copy(alpha = 0.82f))
    ),
    inactiveTrackColor: Color = Kolt.colors.secondarySurface,
    thumbColor: Color = Color.White,
    borderColor: Color = Kolt.colors.dividerColor.copy(alpha = 0.6f),
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) width - thumbSize - 2.dp else 2.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "KoltSwitchThumbOffset"
    )

    val trackAlpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.45f,
        label = "KoltSwitchTrackAlpha"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clip(RoundedCornerShape(height / 2))
            .then(
                if (onCheckedChange != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(bounded = false, radius = height),
                        enabled = enabled,
                        onClick = { onCheckedChange(!checked) }
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        // Track layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (checked) activeTrackBrush else SolidColor(inactiveTrackColor),
                    alpha = trackAlpha
                )
                .border(
                    BorderStroke(
                        width = 0.75.dp,
                        brush = if (checked) activeTrackBrush else SolidColor(borderColor)
                    ),
                    shape = RoundedCornerShape(height / 2)
                )
        )

        // Thumb layer with elevation
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .shadow(elevation = 2.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}
