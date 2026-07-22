package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.velord.ui.feature.setting.SettingScreen
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.compose.viewmodel.koinViewModel

internal object SettingsVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<ThemeVM>()
        val navigator = LocalNavigator.currentOrThrow
        SettingScreen(
            viewModel = viewModel,
            onBackClick = navigator::pop,
        )
    }
}

