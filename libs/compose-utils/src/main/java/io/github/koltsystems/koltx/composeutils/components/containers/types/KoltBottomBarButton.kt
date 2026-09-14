package io.github.koltsystems.koltx.composeutils.components.containers.types

import io.github.koltsystems.koltx.composekmp.wrappers.UiImage

/**
 * A data class representing an item in the bottom navigation bar.
 *
 * @param name The text label to display for the item.
 * @param route The destination route from your NavGraph (typically a serializable object).
 * @param icon The icon to display for the item.
 */
data class KoltBottomBarButton(val name: String, val route: Any, val icon: UiImage)
