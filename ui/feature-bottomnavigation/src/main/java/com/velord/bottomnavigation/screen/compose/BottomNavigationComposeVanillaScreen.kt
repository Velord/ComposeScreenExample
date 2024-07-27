package com.velord.bottomnavigation.screen.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.velord.uicore.utils.ObserveSharedFlow

interface BottomNavigatorVanilla {
    fun getRoute(route: BottomNavigationItem): Any
    fun getStartRoute(route: BottomNavigationItem): Any
    @Composable fun CreateDestinationsNavHostForBottomVanilla(
        navController: NavHostController,
        modifier: Modifier,
        startRoute: BottomNavigationItem
    )
}

private fun onTabClick2(
    isSelected: Boolean,
    item: BottomNavigationItem,
    navController: NavController,
    navigator: BottomNavigatorVanilla,
    onClick: (BottomNavigationItem) -> Unit
) {
    if (isSelected) {
        // When we click again on a bottom bar item and it was already selected
        // we want to pop the back stack until the initial destination of this bottom bar item
        val start = navigator.getStartRoute(item)
        navController.popBackStack(start, false)
        return
    }

    val direction = navigator.getRoute(item)
    navController.navigate(direction) {
        // Pop up to the root of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navController.graph.findStartDestination().id) {
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
fun BottomNavigationComposeVanillaScreen(
    navigator: () -> BottomNavigatorVanilla
) {
    ComposeContent { viewModel, navController, currentTab ->
        ObserveSharedFlow(flow = viewModel.currentTabFlow) { tab ->
            onTabClick2(
                isSelected = tab.isSame,
                item = tab.current,
                navController = navController,
                navigator = navigator(),
                onClick = {}
            )
        }

        Content(
            tab = currentTab,
            createNavHost = {
                navigator().CreateDestinationsNavHostForBottomVanilla(
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