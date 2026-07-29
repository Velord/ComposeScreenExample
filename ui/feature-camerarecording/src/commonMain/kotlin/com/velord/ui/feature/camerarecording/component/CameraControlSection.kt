package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.camerarecording.component.button.OpenVideoFolderButton
import com.velord.ui.feature.camerarecording.component.button.StartStopRecordingButton
import com.velord.ui.feature.camerarecording.component.switcher.audio.CameraAudioSwitcher
import com.velord.ui.feature.camerarecording.component.switcher.lens.CameraLensSwitcher
import com.velord.ui.feature.camerarecording.component.videoQuality.CameraVideoQualityChooser
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
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.86f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
        ),
        tonalElevation = 6.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(0.5f),
                contentAlignment = Alignment.Center,
            ) {
                Box(modifier = Modifier.align(Alignment.CenterStart)) {
                    CameraLensSwitcher(
                        uiState = uiState,
                        onClick = { onAction(CameraRecordingUiAction.ChangeCameraSelector) },
                    )
                }
                CameraVideoQualityChooser(
                    uiState = uiState,
                    onVideoQualityChange = { quality ->
                        onAction(CameraRecordingUiAction.ChangeVideoQuality(quality))
                    },
                )
            }

            StartStopRecordingButton(
                uiState = uiState,
                onClick = { onAction(CameraRecordingUiAction.StartStopRecording) },
                modifier = Modifier.align(Alignment.Center),
            )

            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                OpenVideoFolderButton(
                    uiState = uiState,
                    onClick = { onAction(CameraRecordingUiAction.OpenVideoFolder) },
                )
                CameraAudioSwitcher(
                    uiState = uiState,
                    onAudioEnabledChange = { enabled ->
                        onAction(CameraRecordingUiAction.ChangeIsAudioEnabled(enabled))
                    },
                )
            }
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
