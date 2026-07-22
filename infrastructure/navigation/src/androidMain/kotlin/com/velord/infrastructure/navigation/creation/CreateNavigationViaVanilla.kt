package com.velord.infrastructure.navigation.creation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.velord.infrastructure.navigation.compose.transition.popScaleOutTransition
import com.velord.infrastructure.navigation.compose.vanilla.GraphVanilla
import com.velord.infrastructure.navigation.compose.vanilla.graph.setupMainGraphVanilla
import com.velord.infrastructure.navigation.compose.vanilla.navigator.SupremeNavigatorVanilla

@Composable
internal actual fun CreateNavigationViaVanilla() {
    val navController: NavHostController = rememberNavController()
    val navigator = SupremeNavigatorVanilla(navController)

    // Cannot call NavBackStackEntry.route() before DestinationsNavHost!
    // LogBackStack(navController = navController, tag = "CreateNavigationViaVanilla")

    /*
    Start route is determined by below code. It is NOT like Destinations lib.
    First is startDestination = GraphVanilla.Main.BottomNavigationDestinationVanilla
    That leads to BottomNavigationScreen
    Then CreateNavHostForBottom creates new NavHost
    That leads to startDestination = GraphVanilla.BottomTab.Demo.Self,
    Then certain tab is start route
    Also need to change TabState.Default
    */
    NavHost(
        navController = navController,
        startDestination = GraphVanilla.Main.BottomNavigationDestinationVanilla,
        popExitTransition = { popScaleOutTransition }
    ) {
        setupMainGraphVanilla(navigator)
    }
}
