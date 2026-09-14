package io.github.koltsystems.koltx.composeutils.components.containers.types

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.koltsystems.koltx.composekmp.wrappers.UiColor
import io.github.koltsystems.koltx.composekmp.wrappers.UiImage

data class KoltTopBarButton(
    val icon: UiImage,
    val modifier: Modifier = Modifier,
    val tint: Color = Color.Gray,
    val onClick: () -> Unit,
)
