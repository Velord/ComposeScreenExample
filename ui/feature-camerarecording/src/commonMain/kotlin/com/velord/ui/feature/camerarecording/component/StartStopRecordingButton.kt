package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.model.camera.CameraRecordingState
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState

@Composable
internal fun StartStopRecordingButton(
    uiState: CameraRecordingUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasAudioRequiredPermission = uiState.isAudioEnabled.not() ||
            uiState.permissionState.audio.isGranted
    val hasRequiredPermission = uiState.permissionState.camera.isGranted &&
            hasAudioRequiredPermission
    IconButton(
        onClick = onClick,
        modifier = modifier.size(64.dp),
        enabled = hasRequiredPermission,
    ) {
        val icon = Icons.Filled.run {
            if (uiState.cameraState.recordingState == CameraRecordingState.Idle) Circle else Stop
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(52.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    StartStopRecordingButton(
        uiState = CameraRecordingUiState.DEFAULT,
        onClick = {},
    )
}
