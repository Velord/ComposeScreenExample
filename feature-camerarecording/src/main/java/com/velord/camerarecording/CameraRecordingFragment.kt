package com.velord.camerarecording

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import androidx.navigation.fragment.findNavController
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
    ) { result ->
        Log.d("CameraRecordingFragment", "requestRecordVideoPermissionLauncher: $result")
        val isCameraGranted = result.getOrDefault(
            key = android.Manifest.permission.CAMERA,
            defaultValue = false
        )
        val isAudioGranted = result.getOrDefault(
            key = android.Manifest.permission.RECORD_AUDIO,
            defaultValue = false
        )

        viewModel.updateCameraPermissionState(AndroidPermissionState.invoke(isCameraGranted))
        viewModel.updateAudioPermissionState(AndroidPermissionState.invoke(isAudioGranted))
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
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    Log.d("CameraRecordingFragment", "Lifecycle.State.STARTED")
                    checkRecordVideoPermission()
                }
            }
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.checkPermissionEvent.collect {
                        Log.d("CameraRecordingFragment", "checkPermissionEvent: $it")
                        checkRecordVideoPermission()
                    }
                }
            }
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.navigationEventJetpack.filterNotNull().collect {
                        findNavController().navigate(it.id)
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
    val isRecordingStartedState = viewModel.isRecordingStartedFlow.collectAsStateWithLifecycle()

    Log.d("CameraRecordingFragment", "permissionCameraState: ${permissionCameraState.value}")
    Log.d("CameraRecordingFragment", "permissionAudioState: ${permissionAudioState.value}")
    Content(
        permissionCameraState = permissionCameraState,
        permissionAudioState = permissionAudioState,
        qualityState = qualityState,
        cameraSelectorState = cameraSelectorState,
        isAudioEnabledState = isAudioEnabledState,
        isRecordingStartedState = isRecordingStartedState,
        onCheckPermissionClick = remember { viewModel::onCheckPermission },
        onChangeCameraSelector = remember { viewModel::onChangeVideoCameraSelector },
        onSettingsClick = remember { viewModel::onSettingsClick },
        onStartStopRecording = remember { viewModel::onStartStopRecording },
    )
}

@Composable
private fun Content(
    permissionCameraState: State<AndroidPermissionState>,
    permissionAudioState: State<AndroidPermissionState>,
    qualityState: State<Quality>,
    cameraSelectorState: State<CameraSelector>,
    isAudioEnabledState: State<Boolean>,
    isRecordingStartedState: State<Boolean>,
    onCheckPermissionClick: () -> Unit,
    onChangeCameraSelector: () -> Unit,
    onSettingsClick: () -> Unit,
    onStartStopRecording: (VideoCapture<Recorder>?) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        PermissionInfo(
            permissionCameraState = permissionCameraState,
            permissionAudioState = permissionAudioState,
            isAudioEnabledState = isAudioEnabledState,
            onCheckPermissionClick = onCheckPermissionClick
        )

        Recording(
            permissionCameraState = permissionCameraState,
            permissionAudioState = permissionAudioState,
            qualityState = qualityState,
            cameraSelectorState = cameraSelectorState,
            isRecordingStartedState = isRecordingStartedState,
            onStartStopRecording = onStartStopRecording
        )

        CameraSelector(
            cameraSelectorState = cameraSelectorState,
            onChangeCameraSelector = onChangeCameraSelector
        )

        SettingsIcon(onSettingsClick)
    }
}

@Composable
private fun PermissionInfo(
    permissionCameraState: State<AndroidPermissionState>,
    permissionAudioState: State<AndroidPermissionState>,
    isAudioEnabledState: State<Boolean>,
    onCheckPermissionClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (permissionCameraState.value.isForbidden) {
            PermissionIsNotGrantedState(
                icon = Icons.Filled.CameraAlt,
                label = Rres.string.can_not_get_permission_for_camera,
                onClick = onCheckPermissionClick
            )
        }
        if (permissionAudioState.value.isForbidden && isAudioEnabledState.value) {
            Spacer(modifier = Modifier.size(32.dp))
            PermissionIsNotGrantedState(
                icon = Icons.Filled.PermCameraMic,
                label = Rres.string.can_not_get_permission_for_mic,
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
    permissionCameraState: State<AndroidPermissionState>,
    permissionAudioState: State<AndroidPermissionState>,
    qualityState: State<Quality>,
    cameraSelectorState: State<CameraSelector>,
    isRecordingStartedState: State<Boolean>,
    onStartStopRecording: (VideoCapture<Recorder>?) -> Unit
) {
    // VideoCapture is a generic class that provides a camera stream suitable for video applications.
    // Here we pass the Recorder class which is an implementation of the VideoOutput interface
    // and it allows us to start recording.
    val videoCaptureState: MutableState<VideoCapture<Recorder>?> = remember { mutableStateOf(null) }

    if (permissionCameraState.value.isGranted && permissionAudioState.value.isGranted) {
        CameraRecordingPreview(
            qualityState = qualityState,
            cameraSelectorState = cameraSelectorState,
            onNewVideoCapture = { videoCaptureState.value = it }
        )
        Adjustments(
            isRecordingStartedState = isRecordingStartedState,
            onStartStopClick = { onStartStopRecording(videoCaptureState.value) }
        )
    }
}

@Composable
private fun CameraRecordingPreview(
    qualityState: State<Quality>,
    cameraSelectorState: State<CameraSelector>,
    onNewVideoCapture: (VideoCapture<Recorder>) -> Unit
) {
    val context = LocalContext.current
    // PreviewView is a custom view that will display the camera feed.
    // We will bind it to the lifecycle, add it to the AndroidView
    // and it will show us what we are currently recording.
    val previewView = remember { PreviewView(context) }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(cameraSelectorState.value, qualityState.value) {
        val newVideoCapture = context.createVideoCapture(
            lifecycleOwner = lifecycleOwner,
            cameraSelector = cameraSelectorState.value,
            previewView = previewView,
            preferredQuality = qualityState.value
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
    isRecordingStartedState: State<Boolean>,
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
                isRecordingStartedState = isRecordingStartedState.value,
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
    cameraSelectorState: State<CameraSelector>,
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
        val label = if (cameraSelectorState.value == CameraSelector.DEFAULT_BACK_CAMERA) {
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
        permissionCameraState = mutableStateOf(AndroidPermissionState.Denied),
        permissionAudioState = mutableStateOf(AndroidPermissionState.Denied),
        qualityState = mutableStateOf(Quality.SD),
        cameraSelectorState = mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA),
        isAudioEnabledState = mutableStateOf(true),
        isRecordingStartedState = mutableStateOf(false),
        onCheckPermissionClick = {},
        onChangeCameraSelector = {},
        onSettingsClick = {},
        onStartStopRecording = {}
    )
}