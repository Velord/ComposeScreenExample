package com.velord.composescreenexample.ui.main.bottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.velord.composescreenexample.R
import com.velord.composescreenexample.ui.compose.component.FullSizeBackground
import com.velord.composescreenexample.ui.compose.theme.GunPowder
import com.velord.composescreenexample.ui.compose.theme.RegularAmethystSmoke14Style
import com.velord.composescreenexample.ui.compose.theme.SteelGray
import com.velord.composescreenexample.ui.compose.theme.setContentWithTheme
import com.velord.composescreenexample.ui.compose.utils.getScreenWidthAndHeightInPx
import com.velord.composescreenexample.utils.PermissionState
import com.velord.composescreenexample.utils.context.*
import com.velord.composescreenexample.utils.fragment.checkRecordVideoPermission
import com.velord.composescreenexample.utils.fragment.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomNavFragment : Fragment() {

    private val viewModel by viewModels<BottomNavViewModel>()
    private val recordVideoViewModel by viewModels<RecordVideoViewModel>()

    private val requestRecordVideoPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        val areGranted = it.values.reduce { acc, next -> acc && next }
        recordVideoViewModel.updatePermissionState(PermissionState.invoke(areGranted))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        BottomNavScreen(viewModel, recordVideoViewModel)
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
                recordVideoViewModel.goToSettingsEvent.collect {
                    startActivity(requireContext().createSettingsIntent())
                }
            }
            launch {
                recordVideoViewModel.checkPermissionEvent.collect {
                    checkRecordVideoPermission()
                }
            }
        }
    }

    private fun checkRecordVideoPermission() {
        checkRecordVideoPermission(
            actionLauncher = requestRecordVideoPermissionLauncher,
            onGranted = {
                recordVideoViewModel.updatePermissionState(PermissionState.Granted)
            },
            onDecline = {
                recordVideoViewModel.updatePermissionState(PermissionState.Denied)
            }
        )
    }
}

@Composable
private fun BottomNavScreen(
    viewModel: BottomNavViewModel,
    recordVideoViewModel: RecordVideoViewModel
) {
    val permissionState = recordVideoViewModel.permissionFlow.collectAsStateWithLifecycle()
    val qualityState = recordVideoViewModel.videoQualityFlow.collectAsStateWithLifecycle()
    val cameraSelectorState = recordVideoViewModel.videoCameraSelectorFlow.collectAsStateWithLifecycle()
    val isAudioEnabledState = recordVideoViewModel.videoIsAudioEnabledFlow.collectAsStateWithLifecycle()

    Content(
        permission = permissionState.value,
        quality = qualityState.value,
        cameraSelector = cameraSelectorState.value,
        isAudioEnabled = isAudioEnabledState.value,
        fileMetaData = recordVideoViewModel.createFileMetadata(),
        onCheckPermissionClick = recordVideoViewModel::onCheckPermission,
        onChangeCameraSelector = recordVideoViewModel::onChangeVideoCameraSelector,
        onVideoRecordEvent = recordVideoViewModel::onVideoRecordEvent,
        onNewRecording = recordVideoViewModel::onNewRecording,
        onStopRecording = recordVideoViewModel::onStopRecording
    )
}

@Composable
private fun Content(
    permission: PermissionState,
    quality: Quality,
    cameraSelector: CameraSelector,
    isAudioEnabled: Boolean,
    fileMetaData: RecordVideoMetaData,
    onCheckPermissionClick: () -> Unit,
    onChangeCameraSelector: () -> Unit,
    onVideoRecordEvent: (VideoRecordEvent) -> Unit,
    onNewRecording: (Recording) -> Unit,
    onStopRecording: () -> Unit
) {
    if (permission.isDenied()) {
        val (screenWidthPx, screenHeightPx) = getScreenWidthAndHeightInPx()
        val url = "https://picsum.photos/seed/BottomNavScreen/$screenWidthPx/$screenHeightPx"
        FullSizeBackground(url)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Text(
                text = stringResource(id = R.string.can_not_get_permission),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .background(Color.White)
                    .clickable { onCheckPermissionClick() },
                style = RegularAmethystSmoke14Style
            )
        }
    } else {
        CameraRecordingPreview(
            quality = quality,
            cameraSelector = cameraSelector,
            isAudioEnabled = isAudioEnabled,
            fileMetaData = fileMetaData,
            onChangeCameraSelector = onChangeCameraSelector,
            onVideoRecordEvent = onVideoRecordEvent,
            onNewRecording = onNewRecording,
            onStopRecording = onStopRecording
        )
    }
}

@Composable
private fun CameraRecordingPreview(
    quality: Quality,
    cameraSelector: CameraSelector,
    isAudioEnabled: Boolean,
    fileMetaData: RecordVideoMetaData,
    onChangeCameraSelector: () -> Unit,
    onVideoRecordEvent: (VideoRecordEvent) -> Unit,
    onNewRecording: (Recording) -> Unit,
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
        isAudioEnabled = isAudioEnabled,
        fileMetaData = fileMetaData,
        videoCapture = videoCaptureState.value,
        onChangeCameraSelector = onChangeCameraSelector,
        onVideoRecordEvent = onVideoRecordEvent,
        onNewRecording = onNewRecording,
        onStopRecording = onStopRecording
    )
}

@Composable
private fun Adjustments(
    isAudioEnabled: Boolean,
    fileMetaData: RecordVideoMetaData,
    videoCapture: VideoCapture<Recorder>?,
    onChangeCameraSelector: () -> Unit,
    onVideoRecordEvent: (VideoRecordEvent) -> Unit,
    onNewRecording: (Recording) -> Unit,
    onStopRecording: () -> Unit
) {
    val isRecordingStartedState = remember { mutableStateOf(false) }

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
                isAudioEnabled = isAudioEnabled,
                fileMetaData = fileMetaData,
                videoCapture = videoCapture,
                isRecordingStarted = isRecordingStartedState.value,
                onChangeIsRecordingStarted = {
                    isRecordingStartedState.value = it
                    if (it.not()) {
                        onStopRecording()
                    }
                },
                onNewRecording = onNewRecording,
                onEvent = onVideoRecordEvent
            )
            CameraSelector(
                isRecordingStarted = isRecordingStartedState.value,
                onChangeCameraSelector = onChangeCameraSelector
            )
        }
    }
}

@Composable
private fun StartStop(
    isAudioEnabled: Boolean,
    fileMetaData: RecordVideoMetaData,
    videoCapture: VideoCapture<Recorder>?,
    isRecordingStarted: Boolean,
    onChangeIsRecordingStarted: (Boolean) -> Unit,
    onNewRecording: (Recording) -> Unit,
    onEvent: (VideoRecordEvent) -> Unit
) {
    val context = LocalContext.current

    IconButton(
        onClick = {
            if (isRecordingStarted.not()) {
                videoCapture?.let {
                    onChangeIsRecordingStarted(true)

                    val recording = context.createRecording(
                        fileMetaData = fileMetaData,
                        videoCapture = videoCapture,
                        audioEnabled = isAudioEnabled,
                    ) { event ->
                        onEvent(event)
                    }

                    onNewRecording(recording)
                }
            } else {
                onChangeIsRecordingStarted(false)
            }
        }
    ) {
        val icon =  Icons.Filled.run {
            if (isRecordingStarted) Stop else AddPhotoAlternate
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.SteelGray
        )
    }
}

@Composable
private fun CameraSelector(
    isRecordingStarted: Boolean,
    onChangeCameraSelector: () -> Unit
) {
    if (isRecordingStarted.not()) {
        IconButton(
            onClick = onChangeCameraSelector,
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Filled.SwitchVideo,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.SteelGray
            )
        }
    }
}