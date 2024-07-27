package com.velord.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.navgraphs.MainNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.velord.bottomnavigation.BottomNavVoyagerScreen
import com.velord.bottomnavigation.screen.BottomNavigationComposeVanillaScreen
import com.velord.navigation.compose.destinations.SupremeNavigator
import com.velord.navigation.compose.vanilla.BottomNavigationDestinationVanilla
import com.velord.navigation.compose.vanilla.SupremeVanillaNavigator

@Composable
fun CreateNavigationViaDestinations() {
    val navController: NavHostController = rememberNavController()
    val navigator = SupremeNavigator(navController)

    DestinationsNavHost(
        navGraph = MainNavGraph,
        dependenciesContainerBuilder = {
            dependency(navigator)
        }
    )
}

@Composable
fun CreateNavigationViaVanilla() {
    val navController: NavHostController = rememberNavController()
    val navigator = SupremeVanillaNavigator()

    NavHost(
        navController = navController,
        startDestination = BottomNavigationDestinationVanilla,
    ) {
        composable<BottomNavigationDestinationVanilla> {
            BottomNavigationComposeVanillaScreen(
                navigator = { navigator },
            )
        }
    }
}

@Composable
fun CreateNavigationViaVoyager() {
    Navigator(BottomNavVoyagerScreen) {
        SlideTransition(it)
    }
}