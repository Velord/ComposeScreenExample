package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.flowsummator.FlowSummatorScreen
import com.velord.ui.feature.flowsummator.FlowSummatorViewModel

internal object FlowSummatorVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = viewModel<FlowSummatorViewModel>()
        FlowSummatorScreen(viewModel)
    }
}
