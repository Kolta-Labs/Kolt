package io.github.koltsystems.koltx.composeutils.components.containers.bottomnavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import io.github.koltsystems.koltx.composeutils.components.containers.types.KoltBottomBarButton
import io.github.koltsystems.koltx.composekmp.components.core.HorizontalSpacer
import io.github.koltsystems.koltx.composekmp.components.core.image.KoltIcon
import io.github.koltsystems.koltx.composekmp.components.core.text.KoltText
import io.github.koltsystems.koltx.composekmp.theme.Kolt
import io.github.koltsystems.koltx.composekmp.theme.semiBold
import io.github.koltsystems.koltx.composekmp.wrappers.toUiColor
import io.github.koltsystems.koltx.composekmp.wrappers.toUiImage
import io.github.koltsystems.koltx.composekmp.wrappers.toUiText


/**
 * A convenient wrapper for [KoltBottomNavBar] that integrates directly with a [NavController].
 * It automatically handles the selection state based on the controller's current back stack.
 *
 * @param navController The [NavController] used to handle navigation and track the current destination.
 * @param bottomBarRoutes A list of [KoltBottomBarButton] items to display in the bar.
 * @param visibleItemsCount The maximum number of items to display directly in the bar. If the total
 * number of routes exceeds this, a "More" button will be shown with the rest in an overflow menu.
 * @param modifier A [Modifier] to be applied to the [BottomNavigation] container.
 * @param backgroundColor The background color of the navigation bar.
 * @param selectedColor The color for the icon and text of the currently selected item.
 * @param unselectedColor The color for the icon and text of unselected items.
 * @param selectedBubbleColor The background color for the bubble displayed behind the selected item.
 * @param itemContent A composable lambda that defines the content of each navigation item, allowing for
 *             full customization.
 */
@Composable
fun KoltBottomNavigation(
    navController: NavController,
    bottomBarRoutes: List<KoltBottomBarButton>,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 5,
    backgroundColor: Color = Kolt.colors.mainSurface,
    selectedColor: Color = Kolt.colors.primary,
    unselectedColor: Color = Kolt.colors.onPrimaryCardContainer,
    selectedBubbleColor: Color = Color.Transparent,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentRoute = remember(currentDestination, bottomBarRoutes) {
        bottomBarRoutes.find { barButton ->
            currentDestination?.hierarchy?.any { it.hasRoute(barButton.route::class) } == true
        }?.route
    }

    KoltBottomNavBar(
        routes = bottomBarRoutes,
        currentRoute = currentRoute,
        onRouteClick = {
            navController.navigate(it) {
                launchSingleTop = true
                restoreState = true
            }
        },
        modifier = modifier,
        visibleItemsCount = visibleItemsCount,
        backgroundColor = backgroundColor,
        selectedColor = selectedColor,
        unselectedColor = unselectedColor,
        selectedBubbleColor = selectedBubbleColor,
    )
}

/**
 * A presentation-layer composable for a bottom navigation bar. It is independent of any navigation
 * logic and relies on the provided [currentRoute] to determine the selection state.
 *
 * This structure makes the component easily previewable and testable.
 *
 * @param routes A list of [KoltBottomBarButton] items to display in the bar.
 * @param currentRoute The currently selected route.
 * @param onRouteClick A callback invoked when a navigation item is clicked.
 * @param visibleItemsCount The maximum number of items to display. If the size of [routes] exceeds
 * this, a "More" button will be displayed with the rest in an overflow menu.
 * @param modifier A [Modifier] to be applied to the [BottomNavigation] container.
 * @param backgroundColor The background color of the navigation bar.
 * @param selectedColor The color for the icon and text of the currently selected item.
 * @param unselectedColor The color for the icon and text of unselected items.
 * @param selectedBubbleColor The background color for the bubble displayed behind the selected item.
 * @param itemContent A composable lambda that defines the content of each navigation item.
 */
@Composable
fun KoltBottomNavBar(
    routes: List<KoltBottomBarButton>,
    currentRoute: Any?,
    onRouteClick: (Any) -> Unit,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 5,
    backgroundColor: Color = Kolt.colors.mainSurface,
    selectedColor: Color = Kolt.colors.primary,
    unselectedColor: Color = Kolt.colors.onPrimaryCardContainer,
    selectedBubbleColor: Color = Color.Transparent,
    itemContent: @Composable RowScope.(route: KoltBottomBarButton, isSelected: Boolean, selectedColor: Color, unselectedColor: Color) -> Unit = { route, isSelected, selColor, unselColor ->
        DefaultBottomNavigationItem(
            route = route,
            isSelected = isSelected,
            selectedColor = selColor,
            unselectedColor = unselColor,
            selectedBubbleColor = selectedBubbleColor,
            onClick = { onRouteClick(route.route) }
        )
    }
) {
    val (visibleRoutes, hiddenRoutes) = remember(routes, visibleItemsCount) {
        if (routes.size > visibleItemsCount && visibleItemsCount > 0) {
            val visibleCount = visibleItemsCount - 1
            Pair(routes.take(visibleCount), routes.drop(visibleCount))
        } else {
            Pair(routes, emptyList())
        }
    }

    BottomNavigation(
        backgroundColor = backgroundColor,
        modifier = modifier
    ) {
        HorizontalSpacer(Kolt.sizes.paddingSmall)
        visibleRoutes.forEach { route ->
            val isSelected = route.route == currentRoute
            itemContent(route, isSelected, selectedColor, unselectedColor)
        }

        if (hiddenRoutes.isNotEmpty()) {
            var showMoreMenu by remember { mutableStateOf(false) }
            val isMoreSelected = remember(currentRoute, hiddenRoutes) {
                hiddenRoutes.any { it.route == currentRoute }
            }
            DefaultBottomNavigationItem(
                route = KoltBottomBarButton(
                    name = "More",
                    route = hiddenRoutes.first().route, // Dummy route, click is overridden
                    icon = Icons.Default.MoreHoriz.toUiImage()
                ),
                isSelected = isMoreSelected,
                selectedColor = selectedColor,
                unselectedColor = unselectedColor,
                selectedBubbleColor = selectedBubbleColor,
                onClick = { showMoreMenu = true }
            )
            Box {
                DropdownMenu(
                    expanded = showMoreMenu,
                    onDismissRequest = { showMoreMenu = false }
                ) {
                    hiddenRoutes.forEach { hiddenRoute ->
                        DropdownMenuItem(onClick = {
                            onRouteClick(hiddenRoute.route)
                            showMoreMenu = false
                        }) {
                            Text(text = hiddenRoute.name)
                        }
                    }
                }
            }
        }
        HorizontalSpacer(Kolt.sizes.paddingSmall)
    }
}

/**
 * The default visual implementation for an item in the [KoltBottomNavBar].
 */
@Composable
fun RowScope.DefaultBottomNavigationItem(
    route: KoltBottomBarButton,
    isSelected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    selectedBubbleColor: Color,
    onClick: () -> Unit
) {
    BottomNavigationItem(
        icon = {
            KoltIcon(
                icon = route.icon.setTint(if (isSelected) selectedColor.toUiColor() else unselectedColor.toUiColor()),
                iconHeight = Kolt.sizes.iconMedium
            )
        },
        label = {
            KoltText(
                text = route.name.toUiText(),
                color = if (isSelected) selectedColor else unselectedColor,
                style = if (isSelected) Kolt.typography.textSmall.semiBold else Kolt.typography.textSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = Kolt.sizes.paddingXXSmall)
            )
        },
        selected = isSelected,
        onClick = onClick,
        modifier = Modifier
            .padding(vertical = Kolt.sizes.paddingSmall)
            .background(
                color = if (isSelected) selectedBubbleColor else Color.Transparent,
                shape = RoundedCornerShape(Kolt.sizes.cornerRadiusXLarge)
            )
    )
}

