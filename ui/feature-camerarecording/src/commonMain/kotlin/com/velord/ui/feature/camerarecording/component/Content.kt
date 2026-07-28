package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.velord.core.resource.Res
import com.velord.core.resource.camera
import com.velord.core.ui.compose.component.PlatformScreenHeader
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiAction
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Content(
    uiState: CameraRecordingUiState,
    onAction: (CameraRecordingUiAction) -> Unit,
    onBackClick: (() -> Unit)? = null,
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        CameraPreview(uiState = uiState)
        PlatformScreenHeader(
            modifier = Modifier.statusBarsPadding(),
            title = stringResource(Res.string.camera),
            onBackClick = onBackClick,
        )
        CameraPermissionInfo(
            uiState = uiState,
            onCheckPermissionClick = { onAction(CameraRecordingUiAction.CheckPermissionClick) },
        )
        CameraControlSection(
            uiState = uiState,
            onAction = onAction,
        )
        CameraSettingsButton(
            onClick = { onAction(CameraRecordingUiAction.SettingsClick) },
            enabled = true,
        )
    }
}
