package io.github.koltalabs.kolt.composeutils.components.core.dropdowns

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.koltalabs.kolt.composekmp.components.core.text.KoltText
import io.github.koltalabs.kolt.composekmp.wrappers.UiText


@Composable
fun TextDropDown(
    items: List<UiText>,
    onItemSelected: (index: Int) -> Unit
) {
    DropDownSpinner(
        items = items,
        onSelectedIndexChange = { onItemSelected(it) },
    ) { index, item, _, onClick ->

        KoltText(
            text = item,
            modifier = Modifier.clickable { onClick() }
        )
    }
}