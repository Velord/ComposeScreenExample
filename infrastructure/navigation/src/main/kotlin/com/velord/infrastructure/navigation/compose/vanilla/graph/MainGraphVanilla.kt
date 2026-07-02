package com.velord.infrastructure.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.velord.ui.feature.bottomnavigation.screen.compose.BottomNavigationScreen
import com.velord.infrastructure.navigation.compose.vanilla.GraphVanilla
import com.velord.infrastructure.navigation.compose.vanilla.navigator.SupremeNavigatorVanilla
import com.velord.ui.feature.setting.SettingScreen
import com.velord.ui.sharedviewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

internal fun NavGraphBuilder.setupMainGraphVanilla(navigator: SupremeNavigatorVanilla)  {
    composable<GraphVanilla.Main.BottomNavigationDestinationVanilla> {
        BottomNavigationScreen(navigator = navigator)
    }

    composable<GraphVanilla.Main.SettingDestinationVanilla> {
        val viewModel = koinViewModel<ThemeViewModel>()
        SettingScreen(
            viewModel = viewModel,
            onBackClick = {
               // Handle back navigation if needed
            }
        )
    }
}
