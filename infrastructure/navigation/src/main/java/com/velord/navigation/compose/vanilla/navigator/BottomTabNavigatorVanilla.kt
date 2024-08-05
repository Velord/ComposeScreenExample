package com.velord.navigation.compose.vanilla.navigator

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.velord.bottomnavigation.viewmodel.BottomNavigationItem

interface BottomTabNavigatorVanilla {
    fun getRoute(route: BottomNavigationItem): Any
    fun getStartRoute(route: BottomNavigationItem): Any
}

internal fun onTabClickVanilla(
    isSelected: Boolean,
    item: BottomNavigationItem,
    navController: NavController,
    navigator: BottomTabNavigatorVanilla,
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
}