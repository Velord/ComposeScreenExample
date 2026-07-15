package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.velord.core.resource.Res
import com.velord.core.resource.video_quality_full_hd
import com.velord.core.resource.video_quality_hd
import com.velord.core.resource.video_quality_sd
import com.velord.core.resource.video_quality_ultra_hd
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.model.camera.CameraVideoQuality
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun VideoQualityChooser(
    uiState: CameraRecordingUiState,
    onVideoQualityChange: (CameraVideoQuality) -> Unit,
    modifier: Modifier = Modifier,
) {
    val expandedState = remember { mutableStateOf(false) }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        TextButton(
            onClick = { expandedState.value = true },
            enabled = uiState.isRecordingStarted.not(),
        ) {
            Text(text = stringResource(uiState.videoQuality.label))
        }
        DropdownMenu(
            expanded = expandedState.value,
            onDismissRequest = { expandedState.value = false },
        ) {
            CameraVideoQuality.entries.forEach { quality ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(quality.label)) },
                    onClick = {
                        expandedState.value = false
                        onVideoQualityChange(quality)
                    },
                )
            }
        }
    }
}

private val CameraVideoQuality.label: StringResource get() = when (this) {
    CameraVideoQuality.Sd -> Res.string.video_quality_sd
    CameraVideoQuality.Hd -> Res.string.video_quality_hd
    CameraVideoQuality.FullHd -> Res.string.video_quality_full_hd
    CameraVideoQuality.UltraHd -> Res.string.video_quality_ultra_hd
}

@PreviewCombined
@Composable
private fun Preview() {
    VideoQualityChooser(
        uiState = CameraRecordingUiState.DEFAULT,
        onVideoQualityChange = {},
    )
}
