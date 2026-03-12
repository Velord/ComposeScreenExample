package com.velord.navigation.compose.vanilla.navigator

import android.util.Log
import androidx.navigation.NavController
import com.velord.bottomnavigation.viewmodel.BottomNavigationItem

internal interface BottomTabNavigatorVanilla {
    fun getRouteOnTabClick(route: BottomNavigationItem): Any
    fun getTabStartRoute(route: BottomNavigationItem): Any
    // When popping up to the root of the graph we need Int id
    fun getPopRouteOnTabClick(): Int
}

internal fun onTabClickVanilla(
    isSelected: Boolean,
    item: BottomNavigationItem,
    navController: NavController,
    navigator: BottomTabNavigatorVanilla,
) {
    if (isSelected) {
        Log.d("LogBackStack", "onTabClickVanilla: Selected same tab ($item). Popping to start.")
        // When we click again on a bottom bar item and it was already selected
        // we want to pop the back stack until the initial destination of this bottom bar item
        val start = navigator.getTabStartRoute(item)
        navController.popBackStack(start, false)
        return
    }

    val destination = navigator.getRouteOnTabClick(item)
    Log.d("LogBackStack", "onTabClickVanilla: Navigating to $item")

    navController.navigate(destination) {
        // Pop up to the root of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items

        popUpTo(navigator.getPopRouteOnTabClick()) {
            saveState = true
            // We remove the old tab entirely to keep stacks independent
            // This is how "Destinations" lib works
            inclusive = true
        }

        // Avoid multiple copies of the same destination when reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}