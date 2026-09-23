package io.github.koltalabs.kolt.composekmp.components.core.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import io.github.koltalabs.kolt.composekmp.theme.Kolt
import io.github.koltalabs.kolt.composekmp.wrappers.UiText

@Composable
fun PrefixedText(
    text: UiText,
    modifier: Modifier = Modifier,
    prefix: UiText? = null,
    color: Color = Kolt.colors.onMainSurface,
    prefixColor: Color = Kolt.colors.onMainSurface,
    textStyle: TextStyle = Kolt.typography.textMedium,
    prefixStyle: TextStyle = Kolt.typography.textMedium,
    prefixPadding: Dp = Kolt.sizes.paddingSmall,
    prefixModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier
) {

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        prefix?.let {
            KoltText(
                text = it,
                color = prefixColor,
                style = prefixStyle,
                modifier = prefixModifier.padding(end = prefixPadding)
            )
        }
        KoltText(
            text = text,
            color = color,
            style = textStyle,
            modifier = textModifier
        )
    }
}
