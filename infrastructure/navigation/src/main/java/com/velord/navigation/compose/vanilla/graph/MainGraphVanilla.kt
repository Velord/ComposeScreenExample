package com.velord.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.velord.bottomnavigation.screen.compose.BottomNavigationScreen
import com.velord.navigation.compose.vanilla.BottomNavigationDestinationVanilla
import com.velord.navigation.compose.vanilla.CameraRecordingSettingsDestinationVanilla
import com.velord.navigation.compose.vanilla.navigator.SupremeNavigatorVanilla
import com.velord.settings.SettingsScreen
import com.velord.sharedviewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

internal fun NavGraphBuilder.setupMainGraph(navigator: SupremeNavigatorVanilla)  {
    composable<BottomNavigationDestinationVanilla> {
        BottomNavigationScreen(navigator = navigator)
    }

    composable<CameraRecordingSettingsDestinationVanilla> {
        val viewModel = koinViewModel<ThemeViewModel>()
        SettingsScreen(viewModel)
    }
}