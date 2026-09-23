package io.github.koltalabs.kolt.composekmp.components.containers.types

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.koltalabs.kolt.composekmp.wrappers.UiColor
import io.github.koltalabs.kolt.composekmp.wrappers.UiImage

data class KoltTopBarButton(
    val icon: UiImage,
    val modifier: Modifier = Modifier,
    val tint: Color = Color.Gray,
    val onClick: () -> Unit,
)
