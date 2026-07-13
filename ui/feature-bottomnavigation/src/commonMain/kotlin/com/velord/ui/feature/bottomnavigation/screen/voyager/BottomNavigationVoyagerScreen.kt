package com.velord.ui.feature.bottomnavigation.screen.voyager

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.bottomnavigation.viewmodel.voyager.BottomNavigationVoyagerVM
import org.koin.compose.viewmodel.koinViewModel

object BottomNavigationVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<BottomNavigationVoyagerVM>()
        BottomNavigationVoyagerScreenImpl(viewModel)
    }
}
