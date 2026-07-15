package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiAction
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState

@Composable
internal fun Content(
    uiState: CameraRecordingUiState,
    onAction: (CameraRecordingUiAction) -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        CameraPreview(uiState = uiState)
        CameraPermissionInfo(
            uiState = uiState,
            onCheckPermissionClick = { onAction(CameraRecordingUiAction.CheckPermissionClick) },
        )
        CameraControlSection(
            uiState = uiState,
            onAction = onAction,
        )
        CameraSettingsButton(
            onClick = { onAction(CameraRecordingUiAction.SettingsClick) },
            enabled = true,
        )
    }
}
