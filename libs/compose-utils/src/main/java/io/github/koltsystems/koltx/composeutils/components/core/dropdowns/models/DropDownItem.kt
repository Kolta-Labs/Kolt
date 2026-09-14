package io.github.koltsystems.koltx.composeutils.components.core.dropdowns.models

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.koltsystems.koltx.composekmp.wrappers.UiImage
import io.github.koltsystems.koltx.composekmp.wrappers.UiText

data class DropDownItem(
    val label: UiText,
    val leadingIcon: UiImage? = null,
    val trailingIcon: UiImage? = null,
    val iconPadding: Dp = 8.dp,
    val bottomDivider: Boolean = false,
)
