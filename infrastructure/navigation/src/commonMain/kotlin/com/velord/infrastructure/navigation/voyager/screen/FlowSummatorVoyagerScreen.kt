package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.flowsummator.FlowSummatorScreen
import com.velord.ui.feature.flowsummator.FlowSummatorVM
import org.koin.compose.viewmodel.koinViewModel

internal object FlowSummatorVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<FlowSummatorVM>()
        FlowSummatorScreen(viewModel)
    }
}
