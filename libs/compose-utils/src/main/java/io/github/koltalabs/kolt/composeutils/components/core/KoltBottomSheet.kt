// Moved to compose-kmp. This file is a backward-compatibility wrapper.
// Please migrate imports to: io.github.koltalabs.kolt.composekmp.components.core.messages.KoltBottomSheet
@file:Suppress("unused")

package io.github.koltalabs.kolt.composeutils.components.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.github.koltalabs.kolt.composekmp.theme.Kolt
import io.github.koltalabs.kolt.composekmp.wrappers.UiText

/**
 * @deprecated Moved to compose-kmp. Use [io.github.koltalabs.kolt.composekmp.components.core.messages.KoltBottomSheet].
 */
@Deprecated(
    message = "KoltBottomSheet has been moved to compose-kmp. Use io.github.koltalabs.kolt.composekmp.components.core.messages.KoltBottomSheet",
    replaceWith = ReplaceWith(
        "KoltBottomSheet(showSheet, state, title, dismissSheet, containerColor, showCloseButton, showDragHandle, shape, contentAlignment, contentArrangement, titleAlignment, titlePadding, contentPadding, modifier, content)",
        "io.github.koltalabs.kolt.composekmp.components.core.messages.KoltBottomSheet"
    ),
    level = DeprecationLevel.WARNING
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoltBottomSheet(
    showSheet: Boolean,
    state: SheetState,
    title: UiText? = null,
    dismissSheet: () -> Unit,
    containerColor: Color = Kolt.colors.background,
    showCloseButton: Boolean = true,
    showDragHandle: Boolean = true,
    shape: Shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    contentAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    contentArrangement: Arrangement.Vertical = Arrangement.Top,
    titleAlignment: Arrangement.Horizontal = Arrangement.Center,
    titlePadding: PaddingValues = PaddingValues(16.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    io.github.koltalabs.kolt.composekmp.components.core.messages.KoltBottomSheet(
        showSheet = showSheet,
        state = state,
        title = title,
        dismissSheet = dismissSheet,
        containerColor = containerColor,
        showCloseButton = showCloseButton,
        showDragHandle = showDragHandle,
        shape = shape,
        contentAlignment = contentAlignment,
        contentArrangement = contentArrangement,
        titleAlignment = titleAlignment,
        titlePadding = titlePadding,
        contentPadding = contentPadding,
        modifier = modifier,
        content = content,
    )
}