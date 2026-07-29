package com.velord.ui.feature.camerarecording.component.switcher.audio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velord.core.resource.Res
import com.velord.core.resource.record_audio_off
import com.velord.core.resource.record_audio_on
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CameraAudioSwitcher(
    uiState: CameraRecordingUiState,
    onAudioEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isEnabled = uiState.isAudioEnabled
    val isRecording = uiState.isRecordingStarted
    val label = if (isEnabled) {
        stringResource(Res.string.record_audio_on)
    } else {
        stringResource(Res.string.record_audio_off)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        CameraAudioSwitch(
            isEnabled = isEnabled,
            isRecording = isRecording,
            contentDescription = label,
            onClick = { onAudioEnabledChange(!isEnabled) },
        )
        CameraAudioLabel(label = label)
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    CameraAudioSwitcher(
        uiState = CameraRecordingUiState.DEFAULT,
        onAudioEnabledChange = {},
    )
}
