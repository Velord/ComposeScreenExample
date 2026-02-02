package com.velord.camerarecording

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.camera.core.CameraSelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.PermCameraMic
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.SwitchVideo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.camerarecording.model.createVideoCapture
import com.velord.core.resource.R
import com.velord.core.ui.utils.ObserveSharedFlow
import com.velord.core.ui.utils.permission.CheckCameraAndAudioRecordPermission

enum class CameraRecordingNavigationEvent {
    SETTINGS
}

@Composable
fun CameraRecordingScreen(
    viewModel: CameraRecordingViewModel,
    needToHandlePermission: Boolean = false,
    onNavigationEvent : (CameraRecordingNavigationEvent) -> Unit
) {
    BackHandler {
        Log.d("@@@", "CameraRecordingScreen")
    }

    ObserveSharedFlow(flow = viewModel.navigationEventDestination) {
        onNavigationEvent(it)
    }

    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()

    if (needToHandlePermission) {
        CheckCameraAndAudioRecordPermission(
            triggerCheckEvent = viewModel.checkPermissionEvent,
            onCameraUpdateState = {
                val action = CameraRecordingUiAction.UpdateCameraPermissionState(it)
                viewModel.onAction(action)
            },
            onMicroUpdateState = {
                val action = CameraRecordingUiAction.UpdateAudioPermissionState(it)
                viewModel.onAction(action)
            }
        )
    }

    Log.d("CameraRecordingFragment", "permissionCameraState: ${uiState.value.permissionCamera}")
    Log.d("CameraRecordingFragment", "permissionAudioState: ${uiState.value.permissionAudio}")
    Content(
        uiState = uiState.value,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Content(
    uiState: CameraRecordingUiState,
    onAction: (CameraRecordingUiAction) -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        PermissionInfo(
            uiState = uiState,
            onCheckPermissionClick = { onAction(CameraRecordingUiAction.CheckPermissionClick) }
        )

        Recording(
            uiState = uiState,
            onStartStopRecording = { onAction(CameraRecordingUiAction.StartStopRecording(it)) }
        )

        CameraSelector(
            uiState = uiState,
            onChangeCameraSelector = { onAction(CameraRecordingUiAction.ChangeCameraSelector) }
        )

        SettingsIcon(onClick = { onAction(CameraRecordingUiAction.SettingsClick) })
    }
}

@Composable
private fun PermissionInfo(
    uiState: CameraRecordingUiState,
    onCheckPermissionClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (uiState.permissionCamera.isForbidden) {
            PermissionIsNotGrantedState(
                icon = Icons.Filled.CameraAlt,
                label = R.string.can_not_get_permission_for_camera,
                onClick = onCheckPermissionClick
            )
        }
        if (uiState.permissionAudio.isForbidden && uiState.isAudioEnabled) {
            Spacer(modifier = Modifier.size(32.dp))
            PermissionIsNotGrantedState(
                icon = Icons.Filled.PermCameraMic,
                label = R.string.can_not_get_permission_for_mic,
                onClick = onCheckPermissionClick
            )
        }
    }
}

@Composable
private fun PermissionIsNotGrantedState(
    icon: ImageVector,
    @StringRes label: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
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
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = stringResource(id = label),
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun BoxScope.Recording(
    uiState: CameraRecordingUiState,
    onStartStopRecording: (VideoCapture<Recorder>?) -> Unit
) {
    // VideoCapture is a generic class that provides a camera stream suitable for video applications.
    // Here we pass the Recorder class which is an implementation of the VideoOutput interface
    // and it allows us to start recording.
    val videoCaptureState: MutableState<VideoCapture<Recorder>?> = remember { mutableStateOf(null) }

    val permissionCamera = uiState.permissionCamera
    val permissionAudio = uiState.permissionAudio
    if (permissionCamera.isGranted && permissionAudio.isGranted) {
        CameraRecordingPreview(
            uiState = uiState,
            onNewVideoCapture = { videoCaptureState.value = it }
        )
        Adjustments(
            uiState = uiState,
            onStartStopClick = { onStartStopRecording(videoCaptureState.value) }
        )
    }
}

@Composable
private fun CameraRecordingPreview(
    uiState: CameraRecordingUiState,
    onNewVideoCapture: (VideoCapture<Recorder>) -> Unit
) {
    val context = LocalContext.current
    // PreviewView is a custom view that will display the camera feed.
    // We will bind it to the lifecycle, add it to the AndroidView
    // and it will show us what we are currently recording.
    val previewView = remember { PreviewView(context) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val selector = uiState.cameraSelector
    val quality = uiState.videoQuality
    LaunchedEffect(selector, quality) {
        val newVideoCapture = context.createVideoCapture(
            lifecycleOwner = lifecycleOwner,
            cameraSelector = selector,
            previewView = previewView,
            preferredQuality = quality
        )
        onNewVideoCapture(newVideoCapture)
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun BoxScope.Adjustments(
    uiState: CameraRecordingUiState,
    onStartStopClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .navigationBarsPadding()
            .padding(bottom = 100.dp, end = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StartStop(
                isRecordingStartedState = uiState.isRecordingStarted,
                onClick = onStartStopClick
            )
        }
    }
}

@Composable
private fun StartStop(
    isRecordingStartedState: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        val icon =  Icons.Filled.run {
            if (isRecordingStartedState) Stop else Circle
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun BoxScope.CameraSelector(
    uiState: CameraRecordingUiState,
    onChangeCameraSelector: () -> Unit
) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .navigationBarsPadding()
            .padding(bottom = 100.dp, start = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            ),
        horizontalAlignment = Alignment.Start
    ) {
        val label = if (uiState.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            R.string.rear
        } else {
            R.string.front
        }
        Text(
            text = stringResource(label),
            modifier = Modifier.padding(start = 8.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium,
        )
        CameraSelectorIcon(
            onClick = onChangeCameraSelector
        )
    }
}

@Composable
private fun CameraSelectorIcon(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.SwitchVideo,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun BoxScope.SettingsIcon(
    onClick: () -> Unit
) {
    Icon(
        imageVector = Icons.Filled.SettingsApplications,
        contentDescription = null,
        modifier = Modifier
            .align(Alignment.TopEnd)
            .statusBarsPadding()
            .padding(16.dp)
            .size(40.dp)
            .clickable { onClick() },
        tint = MaterialTheme.colorScheme.surfaceVariant
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun CameraRecordingPreview() {
    Content(
        uiState = CameraRecordingUiState.DEFAULT,
        onAction = {}
    )
}