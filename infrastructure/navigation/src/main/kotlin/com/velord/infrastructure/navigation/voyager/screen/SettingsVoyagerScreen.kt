package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.bottomnavigation.viewmodel.voyager.BottomNavigationVoyagerUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.voyager.BottomNavigationVoyagerVM
import com.velord.ui.feature.setting.SettingScreen
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.compose.viewmodel.koinViewModel

internal object SettingsVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<ThemeVM>()
        val bottomNavVM = koinViewModel<BottomNavigationVoyagerVM>()
        SettingScreen(
            viewModel = viewModel,
            onBackClick = {
                bottomNavVM.onAction(
                    BottomNavigationVoyagerUiAction.UpdateBackHandling(SettingsVoyagerScreen)
                )
            }
        )
    }
}

