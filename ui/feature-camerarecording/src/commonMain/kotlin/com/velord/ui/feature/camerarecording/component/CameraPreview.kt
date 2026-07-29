package com.velord.ui.feature.camerarecording.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState
import com.kashif.cameraK.compose.CameraPreviewView as KameraPreviewView
import com.kashif.cameraK.enums.DeviceOrientation as KameraDeviceOrientation
import com.kashif.cameraK.enums.previewAspectRatio as kameraPreviewAspectRatio
import com.kashif.cameraK.state.CameraKState as KameraState

@Composable
internal fun CameraPreview(uiState: CameraRecordingUiState) {
    val permissionCamera = uiState.permissionState.camera
    val permissionAudio = uiState.permissionState.audio
    val audioIsGranted = uiState.isAudioEnabled.not() || permissionAudio.isGranted
    val hasRequiredPermission = permissionCamera.isGranted && audioIsGranted
    if (hasRequiredPermission.not()) return

    val session = uiState.cameraSession ?: return
    val cameraState = session.value.cameraState.collectAsStateWithLifecycle()
    val readyState = cameraState.value as? KameraState.Ready ?: return

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val coverScale = rememberPreviewCoverScale(readyState = readyState)

        KameraPreviewView(
            controller = readyState.controller,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = coverScale
                    scaleY = coverScale
                },
        )
    }
}

@Composable
private fun BoxWithConstraintsScope.rememberPreviewCoverScale(
    readyState: KameraState.Ready,
): Float {
    val containerWidth = maxWidth
    val containerHeight = maxHeight

    return remember(containerWidth, containerHeight, readyState) {
        calculatePreviewCoverScale(
            containerWidth = containerWidth.value,
            containerHeight = containerHeight.value,
            readyState = readyState,
        )
    }
}

private fun calculatePreviewCoverScale(
    containerWidth: Float,
    containerHeight: Float,
    readyState: KameraState.Ready,
): Float {
    val containerRatio = containerWidth / containerHeight
    val orientation = if (containerRatio >= 1f) {
        KameraDeviceOrientation.LANDSCAPE_LEFT
    } else {
        KameraDeviceOrientation.PORTRAIT
    }
    val previewRatio = readyState.controller
        .getAspectRatio()
        .kameraPreviewAspectRatio(orientation)

    return maxOf(
        containerRatio / previewRatio,
        previewRatio / containerRatio,
    )
}

@PreviewCombined
@Composable
private fun Preview() {
    CameraPreview(uiState = CameraRecordingUiState.DEFAULT)
}
