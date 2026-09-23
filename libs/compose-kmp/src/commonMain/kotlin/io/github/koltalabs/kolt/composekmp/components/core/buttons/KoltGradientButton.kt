package io.github.koltalabs.kolt.composekmp.components.core.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.koltalabs.kolt.composekmp.theme.Kolt
import io.github.koltalabs.kolt.composekmp.wrappers.UiText
import io.github.koltalabs.kolt.composekmp.wrappers.asString

/**
 * Aesthetic gradient action button with high-contrast text and optional leading icon,
 * ideal for primary call-to-action triggers across all themes.
 *
 * @param text The button label as a [UiText].
 * @param onClick Callback invoked when clicked.
 * @param modifier Modifier applied to the button layout.
 * @param icon Optional leading icon.
 * @param enabled Whether the button is interactive.
 * @param shape Corner shape. Defaults to 8.dp rounded corners.
 * @param height Height of the button. Defaults to 36.dp.
 * @param gradient Background gradient brush. Defaults to Kolt primary gradient.
 * @param contentColor Text and icon color. Defaults to Kolt.colors.onPrimary.
 * @param textStyle Typography style for the button text.
 * @param elevation Shadow elevation for the button. Defaults to 2.dp.
 */
@Composable
fun KoltGradientButton(
    text: UiText,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    height: Dp = 36.dp,
    gradient: Brush = Brush.horizontalGradient(
        listOf(Kolt.colors.primary, Kolt.colors.primary.copy(alpha = 0.82f))
    ),
    contentColor: Color = Color.White,
    textStyle: TextStyle = Kolt.typography.body.medium.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
    elevation: Dp = 2.dp,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .height(height)
            .shadow(if (enabled) elevation else 0.dp, shape)
            .clip(shape)
            .background(if (enabled) gradient else Brush.linearGradient(listOf(Kolt.colors.dividerColor, Kolt.colors.dividerColor)))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(15.dp)
                )
            }
            Text(
                text = text.asString(),
                style = textStyle,
                color = contentColor
            )
        }
    }
}

/**
 * Convenience overload accepting a plain [String] for the label.
 */
@Composable
fun KoltGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    height: Dp = 36.dp,
    gradient: Brush = Brush.horizontalGradient(
        listOf(Kolt.colors.primary, Kolt.colors.primary.copy(alpha = 0.82f))
    ),
    contentColor: Color = Color.White,
    textStyle: TextStyle = Kolt.typography.body.medium.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
    elevation: Dp = 2.dp,
) {
    KoltGradientButton(
        text = UiText.DynamicString(text),
        onClick = onClick,
        modifier = modifier,
        icon = icon,
        enabled = enabled,
        shape = shape,
        height = height,
        gradient = gradient,
        contentColor = contentColor,
        textStyle = textStyle,
        elevation = elevation
    )
}
