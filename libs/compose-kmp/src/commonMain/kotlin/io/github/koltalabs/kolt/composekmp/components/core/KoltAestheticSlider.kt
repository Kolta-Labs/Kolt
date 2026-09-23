package io.github.koltalabs.kolt.composekmp.components.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.koltalabs.kolt.composekmp.theme.Kolt

/**
 * Ultra-sleek, aesthetic slider control with primary color gradient on the
 * active track and thumb with high-contrast border and smooth elevation.
 *
 * @param value The current progress value.
 * @param onValueChange Callback invoked when the slider value changes.
 * @param modifier Modifier applied to the slider layout.
 * @param enabled Whether the slider is interactive.
 * @param valueRange Range of values represented by the slider. Defaults to 0f..1f.
 * @param thumbSize Diameter of the slider thumb handle. Defaults to 16.dp.
 * @param trackHeight Thickness of the slider track. Defaults to 6.dp.
 * @param activeTrackBrush Gradient brush applied to the active progress track.
 * @param inactiveTrackColor Color of the inactive progress track.
 * @param thumbBorderColor Border color for the thumb circle. Defaults to White.
 */
@Composable
fun KoltAestheticSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    thumbSize: Dp = 16.dp,
    trackHeight: Dp = 6.dp,
    activeTrackBrush: Brush = Brush.horizontalGradient(
        listOf(Kolt.colors.primary, Kolt.colors.primary.copy(alpha = 0.82f))
    ),
    inactiveTrackColor: Color = Kolt.colors.secondarySurface,
    thumbBorderColor: Color = Color.White,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val rangeSpan = (valueRange.endInclusive - valueRange.start).coerceAtLeast(1e-6f)
    val fraction = ((value - valueRange.start) / rangeSpan).coerceIn(0f, 1f)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(thumbSize.coerceAtLeast(24.dp))
            .pointerInput(enabled, valueRange) {
                if (!enabled) return@pointerInput
                detectTapGestures { offset ->
                    val widthPx = size.width
                    if (widthPx > 0) {
                        val newFraction = (offset.x / widthPx).coerceIn(0f, 1f)
                        val newValue = valueRange.start + newFraction * rangeSpan
                        onValueChange(newValue)
                    }
                }
            }
            .pointerInput(enabled, valueRange) {
                if (!enabled) return@pointerInput
                detectHorizontalDragGestures { change, _ ->
                    change.consume()
                    val widthPx = size.width
                    if (widthPx > 0) {
                        val newFraction = (change.position.x / widthPx).coerceIn(0f, 1f)
                        val newValue = valueRange.start + newFraction * rangeSpan
                        onValueChange(newValue)
                    }
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        val totalWidth = maxWidth
        val activeWidth = totalWidth * fraction

        // Inactive background track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .clip(RoundedCornerShape(trackHeight / 2))
                .background(inactiveTrackColor)
                .border(
                    BorderStroke(0.5.dp, Kolt.colors.dividerColor.copy(alpha = 0.6f)),
                    RoundedCornerShape(trackHeight / 2)
                )
        )

        // Active track with gradient
        if (fraction > 0f) {
            Box(
                modifier = Modifier
                    .width(activeWidth)
                    .height(trackHeight)
                    .clip(RoundedCornerShape(trackHeight / 2))
                    .background(activeTrackBrush)
            )
        }

        // Thumb with gradient fill, high contrast border, and elevation
        val thumbOffset = ((totalWidth - thumbSize) * fraction).coerceAtLeast(0.dp)
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .shadow(elevation = if (isHovered) 4.dp else 2.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(activeTrackBrush)
                .border(
                    BorderStroke(2.dp, thumbBorderColor),
                    CircleShape
                )
        )
    }
}
