package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PermCameraMic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.velord.core.resource.Res
import com.velord.core.resource.can_not_get_permission_for_camera
import com.velord.core.resource.can_not_get_permission_for_mic
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.infrastructure.util.permission.PermissionGrantState
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PermissionInfo(
    uiState: CameraRecordingUiState,
    onCheckPermissionClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        if (uiState.permissionState.camera.isForbidden) {
            PermissionIsNotGrantedState(
                icon = Icons.Filled.CameraAlt,
                label = Res.string.can_not_get_permission_for_camera,
                onClick = onCheckPermissionClick,
            )
        }
        if (uiState.permissionState.audio.isForbidden && uiState.isAudioEnabled) {
            Spacer(modifier = Modifier.size(32.dp))
            PermissionIsNotGrantedState(
                icon = Icons.Filled.PermCameraMic,
                label = Res.string.can_not_get_permission_for_mic,
                onClick = onCheckPermissionClick,
            )
        }
    }
}

@Composable
private fun PermissionIsNotGrantedState(
    icon: ImageVector,
    label: StringResource,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium,
            )
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .padding(4.dp),
            tint = MaterialTheme.colorScheme.error,
        )
        Text(
            text = stringResource(label),
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    val permissionState = CameraRecordingUiState.DEFAULT.permissionState.copy(
        camera = PermissionGrantState.Denied,
        audio = PermissionGrantState.Denied,
    )
    val uiState = CameraRecordingUiState.DEFAULT.copy(permissionState = permissionState)
    PermissionInfo(
        uiState = uiState,
        onCheckPermissionClick = {},
    )
}
