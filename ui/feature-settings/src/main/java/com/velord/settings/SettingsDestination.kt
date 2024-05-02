package com.velord.settings

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<ExternalModuleGraph>
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator,
    screenNumber: Int = 0,
) {
//    val viewModel = viewModel<ThemeViewModel>()
//    SettingsScreen(viewModel)
}