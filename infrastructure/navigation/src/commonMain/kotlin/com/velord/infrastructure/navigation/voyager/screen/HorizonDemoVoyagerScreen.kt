package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.velord.ui.feature.demo.horizon.HorizonDemoScreen
import com.velord.ui.feature.demo.horizon.HorizonDemoVM
import org.koin.compose.viewmodel.koinViewModel

internal object HorizonDemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        HorizonDemoScreen(
            viewModel = koinViewModel<HorizonDemoVM>(),
            onBackClick = { navigator.pop() },
        )
    }
}
