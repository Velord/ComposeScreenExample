package com.velord.camerarecording

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.velord.navigation.ObserveNavigation
import com.velord.uicore.utils.permission.CheckCameraAndAudioRecordPermission
import com.velord.uicore.utils.permission.toPermissionState
import org.koin.androidx.compose.koinViewModel

object CameraRecordingScreen : Screen {

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<CameraRecordingViewModel>()
        ObserveNavigation(viewModel.navigationEventVoyager)

        CheckCameraAndAudioRecordPermission(
            triggerCheckEvent = viewModel.checkPermissionEvent,
            onCameraUpdateState = {
                val state = it.status.toPermissionState()
                viewModel.updateCameraPermissionState(state)
            },
            onMicroUpdateState = {
                val state = it.status.toPermissionState()
                viewModel.updateAudioPermissionState(state)
            }
        )

        CameraRecordingScreen(viewModel)
    }
}