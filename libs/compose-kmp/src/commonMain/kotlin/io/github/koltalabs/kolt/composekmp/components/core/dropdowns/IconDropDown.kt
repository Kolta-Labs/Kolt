package io.github.koltalabs.kolt.composekmp.components.core.dropdowns

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.koltalabs.kolt.composekmp.components.core.image.KoltIcon
import io.github.koltalabs.kolt.composekmp.wrappers.UiImage
import io.github.koltalabs.kolt.composekmp.wrappers.UiText


@Composable
fun IconDropDown(
    items: List<UiText>,
    icon: UiImage,
    modifier: Modifier = Modifier,
    onItemSelected: (index: Int) -> Unit
) {
    DropDownSpinner(
        items = items,
        onSelectedIndexChange = { onItemSelected(it) },
    ) { _, _, _, onClick ->

        KoltIcon(
            icon = icon,
                modifier = modifier.clickable { onClick() }
            )
    }
}