package com.velord.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import com.example.sharedviewmodel.ThemeViewModel
import com.velord.util.context.getActivity

class SettingsScreen : Screen {
    @Composable
    override fun Content() {
        val activity = LocalContext.current.getActivity()
        val viewModel = viewModel<ThemeViewModel>(activity as ViewModelStoreOwner)

        SettingsScreen(viewModel)
    }
}