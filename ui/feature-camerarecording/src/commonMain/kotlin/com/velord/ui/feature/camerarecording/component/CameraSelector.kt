package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwitchVideo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velord.core.resource.Res
import com.velord.core.resource.front
import com.velord.core.resource.rear
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.model.camera.CameraLens
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CameraSelector(
    uiState: CameraRecordingUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val label = if (uiState.cameraState.lens == CameraLens.Back) {
        Res.string.rear
    } else {
        Res.string.front
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelSmall,
        )
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Filled.SwitchVideo,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    CameraSelector(
        uiState = CameraRecordingUiState.DEFAULT,
        onClick = {},
    )
}
