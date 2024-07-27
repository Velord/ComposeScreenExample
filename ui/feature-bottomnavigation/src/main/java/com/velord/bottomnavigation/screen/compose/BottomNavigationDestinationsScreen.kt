package com.velord.bottomnavigation.screen.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.NavHostGraphSpec
import com.ramcosta.composedestinations.spec.RouteOrDirection
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator
import com.velord.uicore.utils.ObserveSharedFlow

interface BottomNavigator {
    fun getDirection(route: BottomNavigationItem): Direction
    fun getStartRoute(route: BottomNavigationItem): RouteOrDirection
    fun getGraph(): NavHostGraphSpec
    @Composable fun CreateDestinationsNavHostForBottom(
        navController: NavHostController,
        modifier: Modifier,
        startRoute: BottomNavigationItem
    )
}

enum class BottomNavigationItem {
    Camera,
    Demo,
    Settings;
}

private fun onTabClick(
    isSelected: Boolean,
    item: BottomNavigationItem,
    destinationNavigator: DestinationsNavigator,
    navigator: BottomNavigator,
    onClick: (BottomNavigationItem) -> Unit
) {
    if (isSelected) {
        // When we click again on a bottom bar item and it was already selected
        // we want to pop the back stack until the initial destination of this bottom bar item
        destinationNavigator.popBackStack(navigator.getStartRoute(item), false)
        return
    }

    val direction = navigator.getDirection(item)
    destinationNavigator.navigate(direction) {
        // Pop up to the root of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navigator.getGraph()) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
    onClick(item)
}

@Composable
fun BottomNavigationDestinationsScreen(
    navigator: BottomNavigator
) {
    ComposeContent { viewModel, navController, currentTab ->
        val destinationNavigator = navController.rememberDestinationsNavigator()

        ObserveSharedFlow(flow = viewModel.currentTabFlow) { tab ->
            onTabClick(
                isSelected = tab.isSame,
                item = tab.current,
                destinationNavigator = destinationNavigator,
                navigator = navigator,
                onClick = {}
            )
        }

        Content(
            tab = currentTab,
            createNavHost = {
                navigator.CreateDestinationsNavHostForBottom(
                    navController = navController,
                    modifier = Modifier
                        .padding(bottom = it.calculateBottomPadding())
                        .fillMaxSize(),
                    startRoute = currentTab
                )
            },
            getNavigationItems = viewModel::getNavigationItems,
            onTabClick = viewModel::onTabClick,
        )
    }
}