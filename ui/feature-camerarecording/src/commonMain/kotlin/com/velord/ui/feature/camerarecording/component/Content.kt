package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.velord.core.resource.Res
import com.velord.core.resource.camera
import com.velord.core.ui.compose.component.PlatformScreenHeader
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.camerarecording.component.button.CameraSettingButton
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiAction
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Content(
    uiState: CameraRecordingUiState,
    onAction: (CameraRecordingUiAction) -> Unit,
    onBackClick: () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        CameraPreview(uiState = uiState)
        HeaderShroud()
        PlatformScreenHeader(
            modifier = Modifier.statusBarsPadding(),
            title = stringResource(Res.string.camera),
            onBackClick = onBackClick,
        )
        PermissionInfo(
            uiState = uiState,
            onCheckPermissionClick = { onAction(CameraRecordingUiAction.CheckPermissionClick) },
        )
        CameraControlSection(
            uiState = uiState,
            onAction = onAction,
        )
        CameraSettingButton(
            onClick = { onAction(CameraRecordingUiAction.SettingsClick) },
            enabled = true,
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    Content(
        uiState = CameraRecordingUiState.DEFAULT,
        onAction = {},
        onBackClick = {},
    )
}
