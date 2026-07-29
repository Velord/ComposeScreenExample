package com.velord.ui.feature.camerarecording.component.videoQuality

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.velord.core.resource.Res
import com.velord.core.resource.video_quality
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.model.camera.config.CameraVideoQuality
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CameraVideoQualityChooser(
    uiState: CameraRecordingUiState,
    onVideoQualityChange: (CameraVideoQuality) -> Unit,
    modifier: Modifier = Modifier,
) {
    val expandedState = remember { mutableStateOf(false) }
    val label = stringResource(Res.string.video_quality)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CameraVideoQualityTrigger(
            quality = uiState.videoQuality,
            isEnabled = uiState.isRecordingStarted.not(),
            contentDescription = label,
            onClick = { expandedState.value = true },
        )
        CameraVideoQualityMenu(
            expanded = expandedState.value,
            selectedQuality = uiState.videoQuality,
            onDismissRequest = { expandedState.value = false },
            onQualityClick = { quality ->
                expandedState.value = false
                onVideoQualityChange(quality)
            },
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    CameraVideoQualityChooser(
        uiState = CameraRecordingUiState.DEFAULT,
        onVideoQualityChange = {},
    )
}
