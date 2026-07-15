package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.velord.core.resource.Res
import com.velord.core.resource.record_audio
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AudioSwitcher(
    uiState: CameraRecordingUiState,
    onAudioEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon = if (uiState.isAudioEnabled) Icons.Filled.Mic else Icons.Filled.MicOff
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Switch(
            checked = uiState.isAudioEnabled,
            onCheckedChange = onAudioEnabledChange,
            enabled = uiState.isRecordingStarted.not(),
            thumbContent = {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(Res.string.record_audio),
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            },
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    AudioSwitcher(
        uiState = CameraRecordingUiState.DEFAULT,
        onAudioEnabledChange = {},
    )
}
