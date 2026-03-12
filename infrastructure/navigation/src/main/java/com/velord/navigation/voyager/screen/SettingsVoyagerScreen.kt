package com.velord.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.bottomnavigation.viewmodel.BottomNavigationVoyagerVM
import com.velord.setting.SettingScreen
import com.velord.sharedviewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

internal object SettingsVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<ThemeViewModel>()
        val bottomNavVM = koinViewModel<BottomNavigationVoyagerVM>()
        SettingScreen(
            viewModel = viewModel,
            onBackClick = {
                bottomNavVM.updateBackHandling(SettingsVoyagerScreen)
            }
        )
    }
}