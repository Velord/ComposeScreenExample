package com.velord.flowsummator

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen

object FlowSummatorScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = viewModel<FlowSummatorViewModel>()
        FlowSummatorScreen(viewModel)
    }
}