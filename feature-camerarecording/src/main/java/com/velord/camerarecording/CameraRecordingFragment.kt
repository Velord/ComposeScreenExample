package com.velord.camerarecording

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.camera.core.CameraSelector
import androidx.camera.video.Quality
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PermCameraMic
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.SwitchVideo
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.velord.camerarecording.model.createVideoCapture
import com.velord.uicore.dialog.checkRecordVideoPermission
import com.velord.uicore.utils.setContentWithTheme
import com.velord.util.fragment.viewLifecycleScope
import com.velord.util.permission.AndroidPermissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import com.velord.resource.R as Rres

@AndroidEntryPoint
class CameraRecordingFragment : Fragment() {

    private val viewModel by viewModels<CameraRecordingViewModel>()

    private val requestRecordVideoPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        val areGranted = it.values.reduce { acc, next -> acc && next }
        viewModel.updatePermissionState(AndroidPermissionState.invoke(areGranted))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        CameraRecordingScreen(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserving()
    }

    private fun initObserving() {
        viewLifecycleScope.launch {
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    checkRecordVideoPermission()
                }
            }
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.checkPermissionEvent.collect {
                        checkRecordVideoPermission()
                    }
                }
            }
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.navigationEvent.filterNotNull().collect {
                        // Obsolete for Voyager
                        //findNavController().navigate(it)
                    }
                }
            }
        }
    }

    private fun checkRecordVideoPermission() {
        checkRecordVideoPermission(
            actionLauncher = requestRecordVideoPermissionLauncher,
            onGranted = {
                viewModel.updatePermissionState(AndroidPermissionState.Granted)
            },
            onDecline = {
                viewModel.updatePermissionState(AndroidPermissionState.Denied)
            }
        )
    }
}

@Composable
internal fun CameraRecordingScreen(viewModel: CameraRecordingViewModel) {
    val permissionCameraState = viewModel.permissionCameraFlow.collectAsStateWithLifecycle()
    val permissionAudioState = viewModel.permissionAudioFlow.collectAsStateWithLifecycle()
    val qualityState = viewModel.videoQualityFlow.collectAsStateWithLifecycle()
    val cameraSelectorState = viewModel.videoCameraSelectorFlow.collectAsStateWithLifecycle()
    val isAudioEnabledState = viewModel.isAudioEnabledFlow.collectAsStateWithLifecycle()

    Content(
        permissionCamera = permissionCameraState.value,
        permissionAudio = permissionAudioState.value,
        quality = qualityState.value,
        cameraSelector = cameraSelectorState.value,
        isAudioEnabled = isAudioEnabledState.value,
        onCheckPermissionClick = viewModel::onCheckPermission,
        onChangeCameraSelector = viewModel::onChangeVideoCameraSelector,
        onNewRecording = viewModel::onNewRecording,
        onStopRecording = viewModel::onStopRecording,
        onSettingsClick = viewModel::onSettingsClick
    )
}

@Composable
private fun Content(
    permissionCamera: AndroidPermissionState,
    permissionAudio: AndroidPermissionState,
    quality: Quality,
    cameraSelector: CameraSelector,
    isAudioEnabled: Boolean,
    onCheckPermissionClick: () -> Unit,
    onChangeCameraSelector: () -> Unit,
    onNewRecording: (VideoCapture<Recorder>) -> Unit,
    onStopRecording: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            if (permissionCamera.isForbidden) {
                PermissionIsNotGrantedState(
                    icon = Icons.Filled.CameraAlt,
                    label = Rres.string.can_not_get_permission_for_camera,
                    onClick = onCheckPermissionClick
                )
            }
            if (permissionAudio.isForbidden && isAudioEnabled) {
                Spacer(modifier = Modifier.size(32.dp))
                PermissionIsNotGrantedState(
                    icon = Icons.Filled.PermCameraMic,
                    label = Rres.string.can_not_get_permission_for_mic,
                    onClick = onCheckPermissionClick
                )
            }
        }

        if (permissionCamera.isGranted && permissionAudio.isGranted) {
            CameraRecordingPreview(
                quality = quality,
                cameraSelector = cameraSelector,
                onNewRecording = onNewRecording,
                onStopRecording = onStopRecording
            )
        }

        CameraSelector(
            cameraSelector = cameraSelector,
            onChangeCameraSelector = onChangeCameraSelector
        )

        SettingsIcon(onSettingsClick)
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
private fun CameraRecordingPreview(
    quality: Quality,
    cameraSelector: CameraSelector,
    onNewRecording: (VideoCapture<Recorder>) -> Unit,
    onStopRecording: () -> Unit
) {
    val context = LocalContext.current
    // PreviewView is a custom view that will display the camera feed.
    // We will bind it to the lifecycle, add it to the AndroidView
    // and it will show us what we are currently recording.
    val previewView = remember { PreviewView(context) }
    // VideoCapture is a generic class that provides a camera stream suitable for video applications.
    // Here we pass the Recorder class which is an implementation of the VideoOutput interface
    // and it allows us to start recording.
    val videoCaptureState: MutableState<VideoCapture<Recorder>?> = remember { mutableStateOf(null) }

    val isRecordingStartedState = remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(cameraSelector) {
        videoCaptureState.value = context.createVideoCapture(
            lifecycleOwner = lifecycleOwner,
            cameraSelector = cameraSelector,
            previewView = previewView,
            preferredQuality = quality
        )
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )

    Adjustments(
        isRecordingStartedState = isRecordingStartedState,
        onStartStopClick = {
            if (isRecordingStartedState.value) {
                isRecordingStartedState.value = false
                onStopRecording()
            } else {
                videoCaptureState.value?.let {
                    isRecordingStartedState.value = true
                    onNewRecording(it)
                }
            }
        }
    )
}

@Composable
private fun Adjustments(
    isRecordingStartedState: State<Boolean>,
    onStartStopClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StartStop(
                isRecordingStarted = isRecordingStartedState.value,
                onClick = onStartStopClick
            )
        }
    }
}

@Composable
private fun StartStop(
    isRecordingStarted: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        val icon =  Icons.Filled.run {
            if (isRecordingStarted) Stop else AddPhotoAlternate
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
    cameraSelector: CameraSelector,
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
        val label = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            Rres.string.rear
        } else {
            Rres.string.front
        }
        Text(
            text = stringResource(label),
            modifier = Modifier.padding(start = 8.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium,
        )
        IconButton(
            onClick = onChangeCameraSelector
        ) {
            Icon(
                imageVector = Icons.Filled.SwitchVideo,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun BoxScope.SettingsIcon(onClick: () -> Unit) {
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

@Preview
@Composable
private fun CameraRecordingPreview() {
    Content(
        permissionCamera = AndroidPermissionState.Denied,
        permissionAudio = AndroidPermissionState.Denied,
        quality = Quality.SD,
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
        isAudioEnabled = true,
        onCheckPermissionClick = {},
        onChangeCameraSelector = {},
        onNewRecording = {},
        onStopRecording = {},
        onSettingsClick = {}
    )
}