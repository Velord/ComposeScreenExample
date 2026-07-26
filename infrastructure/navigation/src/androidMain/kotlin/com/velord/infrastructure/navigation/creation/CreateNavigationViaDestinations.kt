package com.velord.infrastructure.navigation.creation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.navigation.navgraphs.MainNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.velord.infrastructure.navigation.compose.destinations.navigator.SupremeNavigatorDestinations
import com.velord.infrastructure.navigation.compose.log.LogBackStack

@Composable
internal actual fun CreateNavigationViaDestinations() {
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
