package com.velord.navigation.compose.destinations.navigator

import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.NavHostGraphSpec
import com.ramcosta.composedestinations.spec.RouteOrDirection
import com.velord.bottomnavigation.viewmodel.BottomNavigationItem

internal interface BottomTabNavigatorDestinations {
    fun getRouteOnTabClick(route: BottomNavigationItem): Direction
    fun getTabStartRoute(route: BottomNavigationItem): RouteOrDirection
    fun getPopRouteOnTabClick(): NavHostGraphSpec
}

internal fun onTabClickDestinations(
    isSelected: Boolean,
    item: BottomNavigationItem,
    destinationNavigator: DestinationsNavigator,
    navigator: BottomTabNavigatorDestinations,
) {
    if (isSelected) {
        // When we click again on a bottom bar item and it was already selected
        // we want to pop the back stack until the initial destination of this bottom bar item
        val start = navigator.getTabStartRoute(item)
        destinationNavigator.popBackStack(start, false)
        return
    }

    val direction = navigator.getRouteOnTabClick(item)
    destinationNavigator.navigate(direction) {
        // Pop up to the root of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navigator.getPopRouteOnTabClick()) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}