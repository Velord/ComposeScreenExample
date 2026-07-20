package com.velord.infrastructure.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.navigation.navgraphs.MainNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.velord.infrastructure.navigation.compose.LogBackStack
import com.velord.infrastructure.navigation.compose.destinations.navigator.SupremeNavigatorDestinations
import com.velord.infrastructure.navigation.compose.destinations.transition.PopScaleOutTransition
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3
import com.velord.infrastructure.navigation.compose.nav3.graph.setupMainGraphNav3
import com.velord.infrastructure.navigation.compose.nav3.navigator.SupremeNavigatorNav3
import com.velord.infrastructure.navigation.compose.vanilla.GraphVanilla
import com.velord.infrastructure.navigation.compose.vanilla.graph.setupMainGraphVanilla
import com.velord.infrastructure.navigation.compose.vanilla.navigator.SupremeNavigatorVanilla
import com.velord.ui.feature.bottomnavigation.screen.voyager.BottomNavigationVoyagerScreen

@Composable
fun CreateNavigationViaDestinations() {
    val navController: NavHostController = rememberNavController()
    val navigator = SupremeNavigatorDestinations(navController)

    LogBackStack(
        navController = navController,
        tag = "CreateNavigationViaDestinations",
    )
    /*
    Start route is determined by below code. And this parameter @Destination<XGraph>(start = true)
    First is MainGraph
    That leads to BottomNavigationScreen
    Then CreateNavHostForBottom creates new NavHost
    That leads to BottomNavigationGraph
    Then certain tab is start route
    Also need to change TabState.Default to work together.
    */
    DestinationsNavHost(
        navGraph = MainNavGraph,
        navController = navController,
        dependenciesContainerBuilder = {
            dependency(navigator)
        },
    )
}

@Composable
fun CreateNavigationViaVanilla() {
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
        popExitTransition = { PopScaleOutTransition.scaleOut }
    ) {
        setupMainGraphVanilla(navigator)
    }
}

@Composable
fun CreateNavigationViaVoyager() {
    Navigator(BottomNavigationVoyagerScreen) {
        SlideTransition(it)
    }
}

@Composable
fun CreateNavigationViaNav3() {
    val startDestination = GraphNav3.Main.BottomNavigationDestinationNav3
    val backStack = remember { mutableStateListOf<NavKey>(startDestination) }
    val navigator = SupremeNavigatorNav3(backStack)

    /*
    Every navigation implementation follows the same hierarchy: the app host opens the bottom
    navigation host, then the bottom host opens the graph owned by the selected tab.

    Nav3 models both levels explicitly. This outer backStack starts with BottomNavigationScreen
    and receives app-level destinations such as Setting. BottomNavigationScreen delegates its
    nested host to SupremeNavigatorNav3, where NavigationState keeps one saved back stack for each
    top-level tab. Selecting another tab switches the active stack; selecting the current tab pops
    only that tab to its root. Keep this start destination, the NavigationState start route, and
    TabState.DEFAULT aligned when changing the initial screen.
    */

    NavDisplay(
        backStack = backStack, // Custom-managed back stack
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            setupMainGraphNav3(navigator)
        }
    )
}
