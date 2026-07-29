package com.velord.ui.feature.camerarecording.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.velord.core.resource.Res
import com.velord.core.resource.start_recording
import com.velord.core.resource.stop_recording
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingUiState
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun StartStopRecordingButton(
    uiState: CameraRecordingUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasRequiredPermission = uiState.hasRequiredPermission()
    val isRecording = uiState.isRecordingStarted

    StartStopRecordingButtonSurface(
        isRecording = isRecording,
        hasRequiredPermission = hasRequiredPermission,
        actionLabel = startStopRecordingLabel(isRecording = isRecording),
        onClick = onClick,
        modifier = modifier,
    )
}

private fun CameraRecordingUiState.hasRequiredPermission(): Boolean {
    val hasAudioRequiredPermission = isAudioEnabled.not() || permissionState.audio.isGranted

    return permissionState.camera.isGranted && hasAudioRequiredPermission
}

@Composable
private fun startStopRecordingLabel(isRecording: Boolean): String = if (isRecording) {
    stringResource(Res.string.stop_recording)
} else {
    stringResource(Res.string.start_recording)
}

@Composable
private fun StartStopRecordingButtonSurface(
    isRecording: Boolean,
    hasRequiredPermission: Boolean,
    actionLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accentPurple = Color(0xFFC09FF8)
    val ringGradient = createRecordingButtonRingGradient(accentPurple = accentPurple)

    Box(
        modifier = modifier
            .size(82.dp)
            .clip(CircleShape)
            .clickable(
                enabled = hasRequiredPermission,
                onClick = onClick,
            )
            .semantics { contentDescription = actionLabel }
            .graphicsLayer {
                alpha = recordingButtonContentAlpha(
                    hasRequiredPermission = hasRequiredPermission,
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        RecordingButtonGlow(accentPurple = accentPurple)
        RecordingButtonRing(
            isRecording = isRecording,
            ringGradient = ringGradient,
        )
    }
}

private fun createRecordingButtonRingGradient(accentPurple: Color): Brush = Brush.sweepGradient(
    listOf(
        Color(0xFFE5CBFF),
        accentPurple,
        Color(0xFF8E54EC),
        Color(0xFFE5CBFF),
    ),
)

private fun recordingButtonContentAlpha(
    hasRequiredPermission: Boolean,
) = if (hasRequiredPermission) {
    1f
} else {
    0.7f
}

@Composable
private fun RecordingButtonGlow(accentPurple: Color) {
    Box(
        modifier = Modifier
            .size(82.dp)
            .clip(CircleShape)
            .background(accentPurple.copy(alpha = 0.22f)),
    )
    Box(
        modifier = Modifier
            .size(74.dp)
            .clip(CircleShape)
            .background(accentPurple.copy(alpha = 0.34f)),
    )
}

@Composable
private fun RecordingButtonRing(
    isRecording: Boolean,
    ringGradient: Brush,
) {
    Box(
        modifier = Modifier
            .size(68.dp)
            .clip(CircleShape)
            .border(BorderStroke(3.5.dp, ringGradient), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (isRecording) {
            RecordingButtonStopContent()
        } else {
            RecordingButtonStartContent()
        }
    }
}

@Composable
private fun RecordingButtonStopContent() {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE53935)),
    )
}

@Composable
private fun RecordingButtonStartContent() {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color(0xFFF7F4FD)),
    )
}

@PreviewCombined
@Composable
private fun Preview() {
    StartStopRecordingButton(
        uiState = CameraRecordingUiState.DEFAULT,
        onClick = {},
    )
}
