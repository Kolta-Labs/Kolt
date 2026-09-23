package io.github.koltalabs.kolt.composekmp.components.core.messages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.koltalabs.kolt.composekmp.components.core.buttons.KoltTextButton
import io.github.koltalabs.kolt.composekmp.components.core.buttons.types.ButtonStyle
import io.github.koltalabs.kolt.composekmp.components.core.image.KoltIcon
import io.github.koltalabs.kolt.composekmp.components.core.text.KoltText
import io.github.koltalabs.kolt.composekmp.theme.Kolt
import io.github.koltalabs.kolt.composekmp.wrappers.UiImage
import io.github.koltalabs.kolt.composekmp.wrappers.toUiImage
import io.github.koltalabs.kolt.composekmp.wrappers.toUiText

// ── Style enum ─────────────────────────────────────────────────────────────────

/** Semantic intent for [KoltSnackbarHost] / [KoltSnackbarVisuals]. */
enum class SnackbarStyle { Default, Success, Error, Warning, Info }

// ── Visuals model ─────────────────────────────────────────────────────────────

/**
 * Snackbar visuals for [KoltSnackbarHost] — extends [SnackbarVisuals] with an optional
 * leading [icon] and a semantic [style] that drives the container colour.
 *
 * Create via [SnackbarHostState.showAppsSnackbar] rather than instantiating directly.
 */
@Immutable
data class KoltSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    val icon: UiImage? = null,
    val style: SnackbarStyle = SnackbarStyle.Default,
) : SnackbarVisuals

// ── Host ──────────────────────────────────────────────────────────────────────

/**
 * Drop-in replacement for [SnackbarHost] that renders [KoltSnackbarVisuals] with icon
 * support and semantic colour variants.
 *
 * Standard [SnackbarHostState.showSnackbar] calls without [KoltSnackbarVisuals] fall back
 * gracefully to [SnackbarStyle.Default] (M3 inverse-surface colours).
 *
 * ```kotlin
 * val snackbarState = remember { SnackbarHostState() }
 *
 * Scaffold(
 *     snackbarHost = { KoltSnackbarHost(snackbarState) },
 * ) { ... }
 *
 * // In a coroutine / LaunchedEffect:
 * snackbarState.showAppsSnackbar(
 *     message = "Saved",
 *     style = SnackbarStyle.Success,
 * )
 * ```
 *
 * @param hostState The [SnackbarHostState] shared with the enclosing [Scaffold].
 * @param modifier  Applied to the [SnackbarHost] wrapper.
 */
@Composable
fun KoltSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { snackbarData ->
        KoltSnackbarItem(snackbarData = snackbarData)
    }
}

// ── Internal item ─────────────────────────────────────────────────────────────

@Composable
private fun KoltSnackbarItem(snackbarData: SnackbarData) {
    val appsVisuals = snackbarData.visuals as? KoltSnackbarVisuals
    val style = appsVisuals?.style ?: SnackbarStyle.Default
    val icon = appsVisuals?.icon

    val containerColor = snackbarContainerColor(style)
    val contentColor = snackbarContentColor(style)

    Snackbar(
        modifier = Modifier.padding(Kolt.sizes.paddingMedium),
        action = snackbarData.visuals.actionLabel?.let { label ->
            {
                KoltTextButton(
                    text = label.toUiText(),
                    buttonStyle = ButtonStyle.transparent(
                        textColor = contentColor,
                        buttonPressedColor = contentColor.copy(alpha = 0.08f),
                        disabledContentColor = contentColor.copy(alpha = 0.38f),
                    ),
                    onClick = { snackbarData.performAction() },
                )
            }
        },
        dismissAction = if (snackbarData.visuals.withDismissAction) {
            {
                IconButton(onClick = { snackbarData.dismiss() }) {
                    KoltIcon(
                        icon = Icons.Default.Close.toUiImage(),
                        modifier = Modifier.size(Kolt.sizes.iconSmall),
                        tint = contentColor,
                        contentDescription = "Dismiss",
                    )
                }
            }
        } else null,
        shape = RoundedCornerShape(Kolt.sizes.cornerRadiusMedium),
        containerColor = containerColor,
        contentColor = contentColor,
        actionContentColor = contentColor,
        dismissActionContentColor = contentColor,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Kolt.sizes.paddingSmall),
        ) {
            icon?.let {
                KoltIcon(
                    icon = it,
                    modifier = Modifier.size(Kolt.sizes.iconSmall),
                    tint = contentColor,
                )
            }
            KoltText(
                text = snackbarData.visuals.message.toUiText(),
                style = Kolt.typography.body.small,
                color = contentColor,
            )
        }
    }
}

// ── Colour resolvers (private, composable) ────────────────────────────────────

@Composable
private fun snackbarContainerColor(style: SnackbarStyle): Color = when (style) {
    SnackbarStyle.Default -> MaterialTheme.colorScheme.inverseSurface
    SnackbarStyle.Success -> Kolt.colors.successContainer
    SnackbarStyle.Error   -> Kolt.colors.errorContainer
    SnackbarStyle.Warning -> Kolt.colors.warningContainer
    SnackbarStyle.Info    -> Kolt.colors.infoContainer
}

@Composable
private fun snackbarContentColor(style: SnackbarStyle): Color = when (style) {
    SnackbarStyle.Default -> MaterialTheme.colorScheme.inverseOnSurface
    SnackbarStyle.Success -> Kolt.colors.onSuccessContainer
    SnackbarStyle.Error   -> Kolt.colors.onErrorContainer
    SnackbarStyle.Warning -> Kolt.colors.onWarningContainer
    SnackbarStyle.Info    -> Kolt.colors.onInfoContainer
}

// ── Extension ─────────────────────────────────────────────────────────────────

/**
 * Shows a snackbar with [KoltSnackbarVisuals] — supports a leading [icon] and a semantic
 * [style] that drives the container/content colours in [KoltSnackbarHost].
 *
 * This is a suspend function and must be called from a coroutine or [LaunchedEffect].
 */
suspend fun SnackbarHostState.showAppsSnackbar(
    message: String,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Short,
    icon: UiImage? = null,
    style: SnackbarStyle = SnackbarStyle.Default,
): SnackbarResult = showSnackbar(
    KoltSnackbarVisuals(
        message = message,
        actionLabel = actionLabel,
        withDismissAction = withDismissAction,
        duration = duration,
        icon = icon,
        style = style,
    )
)
