package com.velord.ui.feature.camerarecording.component.switcher.lens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState

@Composable
internal fun CameraLensSwitcher(
    uiState: CameraRecordingUiState,
    onClick: () -> Unit,
) {
    val isFront = uiState.cameraState.lens.isFront
    val accentColor = Color(0xFFA87BF7)
    val isEnabled = uiState.isCameraLensSwitchAvailable

    Column(
        modifier = Modifier.width(78.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        CameraLensSwitchSurface(
            isFront = isFront,
            accentColor = accentColor,
            isEnabled = isEnabled,
            onClick = onClick,
        )
        CameraLensLabelRow(
            isFront = isFront,
            accentColor = accentColor,
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    CameraLensSwitcher(
        uiState = CameraRecordingUiState.DEFAULT,
        onClick = {},
    )
}
