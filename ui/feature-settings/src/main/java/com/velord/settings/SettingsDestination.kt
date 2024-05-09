package com.velord.settings

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.velord.sharedviewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

@Destination<ExternalModuleGraph>
@Composable
fun SettingsBottomGraphDestination() {
    val viewModel = koinViewModel<ThemeViewModel>()
    SettingsScreen(viewModel)
}

@Destination<ExternalModuleGraph>
@Composable
fun SettingsCameraRecordingGraphDestination() {
    val viewModel = koinViewModel<ThemeViewModel>()
    SettingsScreen(viewModel)
}