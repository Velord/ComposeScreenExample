package com.velord.infrastructure.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.infrastructure.navigation.compose.vanilla.GraphVanilla
import com.velord.infrastructure.navigation.compose.vanilla.navigator.BottomNavigatorVanilla
import com.velord.ui.feature.setting.SettingScreen
import com.velord.ui.sharedviewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

internal fun NavGraphBuilder.setupBottomNavigationGraphVanilla(navigator: BottomNavigatorVanilla) {
    setupCameraRecordingGraphVanilla(navigator)
    setupDemoGraphVanilla(navigator)

    composable<GraphVanilla.BottomTab.SettingDestinationVanilla> {
        val viewModel = koinViewModel<ThemeViewModel>()
        val bottomNavVM = koinViewModel<BottomNavigationDestinationsVM>()
        SettingScreen(
            viewModel = viewModel,
            onBackClick = {
                bottomNavVM.graphCompletedHandling()
            }
        )
    }
}
