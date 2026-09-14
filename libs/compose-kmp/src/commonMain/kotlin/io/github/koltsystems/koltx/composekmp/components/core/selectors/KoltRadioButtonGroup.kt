package io.github.koltsystems.koltx.composekmp.components.core.selectors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.koltsystems.koltx.composekmp.theme.Kolt

/**
 * A single option within a [KoltRadioButtonGroup].
 */
data class KoltRadioOption<T>(
    val value: T,
    val label: String,
    val enabled: Boolean = true,
)

/**
 * Vertical, single-selection group of [KoltRadioButton]s, one row per [options] entry.
 * Only one option can be selected at a time; clicking the row or the radio button selects it.
 *
 * @param options Options to render, in order.
 * @param selectedValue The currently selected option's [KoltRadioOption.value].
 * @param onOptionSelected Callback invoked with the newly selected value.
 * @param modifier Modifier applied to the outer column.
 * @param enabled Whether the whole group is interactive; overridden per-option by [KoltRadioOption.enabled].
 * @param radioSize Diameter of each radio button. Defaults to 18.dp.
 * @param radioShape Shape of each radio button; defaults to [CircleShape]. Pass a
 * [androidx.compose.foundation.shape.RoundedCornerShape] for a rounded-square style.
 * @param spacing Vertical spacing between rows.
 * @param activeBrush Gradient brush applied to the selected radio button.
 * @param inactiveBorderColor Color of the ring for unselected radio buttons.
 */
@Composable
fun <T> KoltRadioButtonGroup(
    options: List<KoltRadioOption<T>>,
    selectedValue: T?,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    radioSize: Dp = 18.dp,
    radioShape: Shape = CircleShape,
    spacing: Dp = Kolt.sizes.paddingSmall,
    activeBrush: Brush = Brush.horizontalGradient(
        listOf(Kolt.colors.primary, Kolt.colors.primary.copy(alpha = 0.82f))
    ),
    inactiveBorderColor: Color = Kolt.colors.dividerColor.copy(alpha = 0.8f),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        options.forEach { option ->
            val rowEnabled = enabled && option.enabled
            val isSelected = option.value == selectedValue
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isSelected,
                        enabled = rowEnabled,
                        role = Role.RadioButton,
                        onClick = { onOptionSelected(option.value) }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Kolt.sizes.paddingSmall)
            ) {
                KoltRadioButton(
                    selected = isSelected,
                    onClick = null,
                    enabled = rowEnabled,
                    size = radioSize,
                    shape = radioShape,
                    activeBrush = activeBrush,
                    inactiveBorderColor = inactiveBorderColor,
                )
                Text(text = option.label, color = Kolt.colors.onMainSurface)
            }
        }
    }
}
