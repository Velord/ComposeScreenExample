package com.velord.settings

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.sharedviewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

object SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<ThemeViewModel>()
        SettingsScreen(viewModel)
    }
}