package com.velord.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.velord.bottomnavigation.screen.compose.BottomNavigationScreen
import com.velord.navigation.compose.vanilla.GraphVanilla
import com.velord.navigation.compose.vanilla.navigator.SupremeNavigatorVanilla
import com.velord.setting.SettingScreen
import com.velord.sharedviewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

internal fun NavGraphBuilder.setupMainGraph(navigator: SupremeNavigatorVanilla)  {
    composable<GraphVanilla.Main.BottomNavigationDestinationVanilla> {
        BottomNavigationScreen(navigator = navigator)
    }

    composable<GraphVanilla.Main.SettingDestinationVanilla> {
        val viewModel = koinViewModel<ThemeViewModel>()
        SettingScreen(viewModel)
    }
}