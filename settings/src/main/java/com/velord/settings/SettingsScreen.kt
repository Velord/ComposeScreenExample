package com.velord.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.sharedviewmodel.ThemeViewModel
import com.velord.util.context.getActivity

class SettingsScreen : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(id = com.velord.resource.R.string.settings)
            val icon = rememberVectorPainter(image = Icons.Outlined.Settings)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val activity = LocalContext.current.getActivity()
        val viewModel = viewModel<ThemeViewModel>(activity as ViewModelStoreOwner)

        SettingsScreen(viewModel)
    }
}