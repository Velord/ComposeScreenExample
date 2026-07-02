package com.velord.ui.feature.bottomnavigation.screen.voyager

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVoyagerVM
import org.koin.androidx.compose.koinViewModel

object BottomNavigationVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<BottomNavigationVoyagerVM>()
        VoyagerScreen(viewModel)
    }
}
