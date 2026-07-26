package com.velord.ui.feature.camerarecording.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.velord.core.resource.Res
import com.velord.core.resource.open_video_folder
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OpenVideoFolderButton(
    uiState: CameraRecordingUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.lastVideoAsset == null) return

    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Filled.FolderOpen,
            contentDescription = stringResource(Res.string.open_video_folder),
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    OpenVideoFolderButton(
        uiState = CameraRecordingUiState.DEFAULT,
        onClick = {},
    )
}
