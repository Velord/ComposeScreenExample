package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiAction
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState

@Composable
internal fun BoxScope.CameraControlSection(
    uiState: CameraRecordingUiState,
    onAction: (CameraRecordingUiAction) -> Unit,
) {
    Surface(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.92f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CameraSelector(
                uiState = uiState,
                onClick = { onAction(CameraRecordingUiAction.ChangeCameraSelector) },
                modifier = Modifier.weight(1f),
            )
            VideoQualityChooser(
                uiState = uiState,
                onVideoQualityChange = { quality ->
                    onAction(CameraRecordingUiAction.ChangeVideoQuality(quality))
                },
                modifier = Modifier.weight(1f),
            )
            StartStopRecordingButton(
                uiState = uiState,
                onClick = { onAction(CameraRecordingUiAction.StartStopRecording) },
                modifier = Modifier.weight(1f),
            )
            AudioSwitcher(
                uiState = uiState,
                onAudioEnabledChange = { enabled ->
                    onAction(CameraRecordingUiAction.ChangeIsAudioEnabled(enabled))
                },
                modifier = Modifier.weight(2f),
            )
            OpenVideoFolderButton(
                uiState = uiState,
                onClick = { onAction(CameraRecordingUiAction.OpenVideoFolder) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    Box {
        CameraControlSection(
            uiState = CameraRecordingUiState.DEFAULT,
            onAction = {},
        )
    }
}
