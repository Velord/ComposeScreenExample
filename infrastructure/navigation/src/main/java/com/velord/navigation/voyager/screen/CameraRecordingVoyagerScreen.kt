package com.velord.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.camerarecording.CameraRecordingScreen
import com.velord.camerarecording.CameraRecordingViewModel
import com.velord.navigation.voyager.ObserveNavigation
import org.koin.androidx.compose.koinViewModel

object CameraRecordingVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<CameraRecordingViewModel>()
        ObserveNavigation(viewModel.navigationEventVoyager)
        CameraRecordingScreen(viewModel, true) {}
    }
}