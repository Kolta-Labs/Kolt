package io.github.koltsystems.koltx.composeutils.components.containers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.koltsystems.koltx.composekmp.components.core.image.KoltIcon
import io.github.koltsystems.koltx.composeutils.components.containers.types.KoltTopBarButton
import io.github.koltsystems.koltx.composekmp.components.core.HorizontalSpacer
import io.github.koltsystems.koltx.composekmp.components.core.buttons.KoltIconButton
import io.github.koltsystems.koltx.composekmp.components.core.image.KoltImage
import io.github.koltsystems.koltx.composekmp.components.core.text.KoltText
import io.github.koltsystems.koltx.composekmp.theme.Kolt
import io.github.koltsystems.koltx.composekmp.theme.Kolt.sizes
import io.github.koltsystems.koltx.composekmp.theme.noPadding
import io.github.koltsystems.koltx.composekmp.theme.semiBold
import io.github.koltsystems.koltx.composekmp.wrappers.toUiColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoltTopBar(
    navMode: NavigationMode = NavigationMode.EMPTY,
    navIconClick: (() -> Unit)? = null,
    appBarTitle: AppBarTitle?,
    background: Color = Kolt.colors.topAppBar,
    onTopBarColor: Color = Kolt.colors.onTopAppBar,
    actions: List<KoltTopBarButton>? = null,
    actionsContent: @Composable RowScope.(Color) -> Unit = {},
) {
    val backgroundColor = background
    val contentColor = onTopBarColor
    TopAppBar(
        navigationIcon = {
            if (navMode != NavigationMode.EMPTY) {
                KoltIconButton(
                    icon = navMode.icon,
                    iconModifier = Modifier.size(sizes.iconStandard),
                    onClick = {
                        navIconClick?.invoke()
                    }
                )
            } else HorizontalSpacer()
        },
        title = {
            appBarTitle?.let {
                AppBarTitleImage(
                    appBarTitle = it,
                    tintColor = onTopBarColor,
                )
            }
        },
        colors = TopAppBarColors(
            containerColor = backgroundColor,
            scrolledContainerColor = backgroundColor,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor
        ),
        actions = {
            actions?.forEach { btn ->
                KoltIcon(
                    icon = btn.icon.setTint(tint = onTopBarColor.toUiColor()),
                    modifier = btn.modifier
                        .padding(end = 4.dp)
                        .size(sizes.actionButtonSize)
                        .clickable { btn.onClick.invoke() }
                        .padding(12.dp),
                )
            }
            actionsContent(contentColor)
        }
    )
}


@Composable
fun AppBarTitleImage(
    appBarTitle: AppBarTitle,
    tintColor: Color = Kolt.colors.onTopAppBar,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = appBarTitle.modifier
    ) {
        when (appBarTitle) {
            is AppBarTitle.BrandLogo -> {
                KoltImage(
                    image = appBarTitle.image,
                    modifier = Modifier.wrapContentWidth()
                )
            }

            is AppBarTitle.ScreenTitle -> {
                KoltText(
                    text = appBarTitle.title,
                    style = Kolt.typography.textMediumLarge.semiBold,
                    color = tintColor
                )
            }

            is AppBarTitle.ScreenTitleWithIcon -> {
                KoltIcon(
                    icon = appBarTitle.icon,
                    modifier = Modifier
                        .size(appBarTitle.iconHeight)
                        .padding(end = appBarTitle.iconPadding),

                    iconHeight = null
                )
                Column(verticalArrangement = Arrangement.spacedBy(sizes.paddingXXSmall)) {
                    KoltText(
                        text = appBarTitle.title,
                        style = appBarTitle.titleStyle?:Kolt.typography.textMediumLarge.semiBold.noPadding,
                        color = tintColor,
                        modifier = Modifier.offset(y=1.dp)
                    )
                    appBarTitle.subTitle?.let {
                        KoltText(
                            text = it,
                            style = appBarTitle.subTitleStyle
                                ?: Kolt.typography.textSmall.noPadding,
                            color = tintColor,
                            modifier = Modifier.offset(y = 1.dp)
                        )
                    }
                }

            }

            AppBarTitle.None -> {}
        }
    }
}