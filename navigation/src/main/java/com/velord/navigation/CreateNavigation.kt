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
import com.velord.bottomnavigation.screen.compose.BottomNavigationComposeVanillaScreen
import com.velord.bottomnavigation.screen.compose.LogBackStack
import com.velord.bottomnavigation.screen.voyager.BottomNavigationVoyagerScreen
import com.velord.navigation.compose.destinations.navigator.SupremeNavigator
import com.velord.navigation.compose.vanilla.BottomNavigationDestinationVanilla
import com.velord.navigation.compose.vanilla.CameraRecordingSettingsDestinationVanilla
import com.velord.navigation.compose.vanilla.SupremeVanillaNavigator
import com.velord.settings.SettingsScreen
import com.velord.sharedviewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateNavigationViaDestinations() {
    val navController: NavHostController = rememberNavController()
    val navigator = SupremeNavigator(navController)

    LogBackStack(navController = navController, tag = "CreateNavigationViaDestinations")

    DestinationsNavHost(
        navGraph = MainNavGraph,
        navController = navController,
        dependenciesContainerBuilder = {
            dependency(navigator)
        }
    )
}

@Composable
fun CreateNavigationViaVanilla() {
    val navController: NavHostController = rememberNavController()
    val navigator = SupremeVanillaNavigator(navController)

    //LogBackStack(navController = navController, tag = "CreateNavigationViaVanilla")

    NavHost(
        navController = navController,
        startDestination = BottomNavigationDestinationVanilla,
    ) {
        composable<BottomNavigationDestinationVanilla> {
            BottomNavigationComposeVanillaScreen(
                navigator = { navigator },
            )
        }

        composable<CameraRecordingSettingsDestinationVanilla> {
            val viewModel = koinViewModel<ThemeViewModel>()
            SettingsScreen(viewModel)
        }
    }
}

@Composable
fun CreateNavigationViaVoyager() {
    Navigator(BottomNavigationVoyagerScreen) {
        SlideTransition(it)
    }
}