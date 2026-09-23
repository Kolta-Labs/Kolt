package io.github.koltalabs.kolt.composeutils.components.core.dropdowns

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.koltalabs.kolt.composekmp.components.core.image.KoltIcon
import io.github.koltalabs.kolt.composekmp.wrappers.UiImage
import io.github.koltalabs.kolt.composekmp.wrappers.toUiImage
import io.github.koltalabs.kolt.composekmp.components.core.text.KoltText
import io.github.koltalabs.kolt.composekmp.wrappers.UiText
import io.github.koltalabs.kolt.composekmp.wrappers.toUiText
import io.github.koltalabs.kolt.composekmp.theme.Kolt


@Composable
fun ChipDropDown(
    items: List<UiText>,
    label: UiText,
    leadingIcon: UiImage? = null,
    trailingIcon: UiImage? = null,
    chipBackground: Color = AssistChipDefaults.assistChipColors().containerColor,
    chipTextColor: Color = AssistChipDefaults.assistChipColors().labelColor,
    chipTextStyle: TextStyle = Kolt.typography.body.medium,
    onItemSelected: (index: Int) -> Unit
) {
    DropDownSpinner(
        items = items,
        onSelectedIndexChange = { onItemSelected(it) },
    ) { _, _, _, onClick ->

        AssistChip(
            onClick = onClick,
            label = {
                KoltText(
                    text = label,
                    style = chipTextStyle,
                    modifier = Modifier.offset(y = 1.dp)
                )
            },
            leadingIcon = { leadingIcon?.let { KoltIcon(it) } },
            trailingIcon = { trailingIcon?.let { KoltIcon(it) } },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = chipBackground,
                labelColor = chipTextColor
            )
        )
    }
}


@Preview
@Composable
fun PreviewChipDropDown() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ChipDropDown(
            items = listOf("Item 1", "Item 2", "Item 3").map { it.toUiText() },
            label = "Select Item".toUiText(),
            onItemSelected = {},
            chipBackground = Color.White
        )

        ChipDropDown(
            items = listOf("Item 1", "Item 2", "Item 3").map { it.toUiText() },
            label = "Select Item".toUiText(),
            onItemSelected = {},
            chipBackground = Color.White,
            leadingIcon = Icons.Default.CalendarMonth.toUiImage(),
        )
    }
}