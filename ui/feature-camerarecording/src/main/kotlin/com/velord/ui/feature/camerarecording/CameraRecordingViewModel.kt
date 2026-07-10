package com.velord.ui.feature.camerarecording

import androidx.camera.core.CameraSelector
import androidx.camera.video.Quality
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import com.velord.core.navigation.fragment.NavigationDataFragment
import com.velord.core.navigation.fragment.entryPoint.SettingsSourceFragment
import com.velord.core.navigation.voyager.NavigationDataVoyager
import com.velord.core.navigation.voyager.SharedScreenVoyager
import com.velord.core.resource.R
import com.velord.infrastructure.util.permission.PermissionGrantState
import com.velord.model.camera.RecordingSession
import com.velord.model.camera.VideoCaptureWrapper
import com.velord.ui.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.camera.StartRecordingUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CameraRecordingUiState(
    // Permission
    val permissionCamera: PermissionGrantState,
    val permissionAudio: PermissionGrantState,
    // Video control
    val videoQuality: Quality,
    val cameraSelector: CameraSelector,
    val isAudioEnabled: Boolean,
    val isRecordingStarted: Boolean,
    // A RecordingSession controls the current active recording without exposing CameraX Recording.
    val recording: RecordingSession?,
) {
    companion object {
        val DEFAULT = CameraRecordingUiState(
            permissionCamera = PermissionGrantState.NotAsked,
            permissionAudio = PermissionGrantState.NotAsked,
            videoQuality = Quality.HIGHEST,
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA,
            isAudioEnabled = true,
            isRecordingStarted = false,
            recording = null,
        )
    }
}

sealed interface CameraRecordingUiAction {
    data object SettingsClick : CameraRecordingUiAction
    data object ChangeCameraSelector : CameraRecordingUiAction
    data object CheckPermissionClick : CameraRecordingUiAction
    data class ChangeVideoQuality(val quality: Quality) : CameraRecordingUiAction
    data class ChangeIsAudioEnabled(val enabled: Boolean) : CameraRecordingUiAction
    data class StartStopRecording(val newCapture: VideoCapture<Recorder>?) : CameraRecordingUiAction
    data class UpdateCameraPermissionGrantState(val state: PermissionGrantState) : CameraRecordingUiAction
    data class UpdateAudioPermissionGrantState(val state: PermissionGrantState) : CameraRecordingUiAction
    data class UpdatePermissionGrantState(val state: PermissionGrantState) : CameraRecordingUiAction
}

class CameraRecordingViewModel(
    private val startRecording: StartRecordingUC,
) : CoroutineScopeViewModel() {

    val uiStateFlow = MutableStateFlow(CameraRecordingUiState.DEFAULT)
    // User interaction
    val checkPermissionEvent = MutableSharedFlow<Unit>()
    val navigationEventVoyager = MutableSharedFlow<NavigationDataVoyager?>()
    val navigationEventJetpack = MutableSharedFlow<NavigationDataFragment?>()
    val navigationEvent = MutableSharedFlow<CameraRecordingNavigationEvent>()
    private val actionFlow = MutableSharedFlow<CameraRecordingUiAction>()

    init {
        observe()
    }

    fun onAction(action: CameraRecordingUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onSettingsClick() {
        launch {
            val nav = NavigationDataVoyager(
                screen = SharedScreenVoyager.BottomNavigationTab.Settings,
                useRoot = true
            )
            navigationEventVoyager.emit(nav)

            val data = NavigationDataFragment(
                id = R.id.from_cameraRecordingFragment_to_settingsFragment,
                payload = SettingsSourceFragment.CameraRecording.encode(),
            )
            navigationEventJetpack.emit(data)
            navigationEvent.emit(CameraRecordingNavigationEvent.SETTINGS)
        }
    }

    private fun updatePermissionGrantState(state: PermissionGrantState) {
        updateCameraPermissionGrantState(state)
        updateAudioPermissionGrantState(state)
    }

    private fun updateCameraPermissionGrantState(state: PermissionGrantState) {
        uiStateFlow.update {
            it.copy(permissionCamera = state)
        }
    }

    private fun updateAudioPermissionGrantState(state: PermissionGrantState) {
        uiStateFlow.update {
            it.copy(permissionAudio = state)
        }
    }

    private fun onCheckPermission() {
        launch {
            checkPermissionEvent.emit(Unit)
        }
    }

    private fun onChangeVideoQuality(quality: Quality) {
        uiStateFlow.update {
            it.copy(videoQuality = quality)
        }
    }

    private fun onChangeVideoCameraSelector() {
        val selector = uiStateFlow.value.cameraSelector
        val isBackCamera = selector == CameraSelector.DEFAULT_BACK_CAMERA
        val newCamera = if (isBackCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        uiStateFlow.update {
            it.copy(cameraSelector = newCamera)
        }
    }

    private fun onChangeIsAudioEnabled(enabled: Boolean) {
        uiStateFlow.update {
            it.copy(isAudioEnabled = enabled)
        }
    }

    private fun onStartStopRecording(newCapture: VideoCapture<Recorder>?) {
        val isStarted = uiStateFlow.value.isRecordingStarted
        if (isStarted) {
            uiStateFlow.update {
                it.copy(isRecordingStarted = false)
            }
            onStopRecording()
        } else {
            newCapture?.let {
                uiStateFlow.update { state ->
                    state.copy(isRecordingStarted = true)
                }
                onNewRecording(it)
            }
        }
    }

    private fun onNewRecording(newCapture: VideoCapture<Recorder>) {
        launch {
            val newRecording = startRecording(
                videoCapture = VideoCaptureWrapper(newCapture),
                audioEnabled = uiStateFlow.value.isAudioEnabled,
            )
            uiStateFlow.update {
                it.copy(recording = newRecording)
            }
        }
    }

    private fun onStopRecording() {
        uiStateFlow.value.recording?.stop()
    }

    private fun observe() {
        launch {
            actionFlow.collect { action ->
                when (action) {
                    is CameraRecordingUiAction.SettingsClick -> onSettingsClick()
                    is CameraRecordingUiAction.ChangeCameraSelector -> onChangeVideoCameraSelector()
                    is CameraRecordingUiAction.ChangeVideoQuality -> onChangeVideoQuality(action.quality)
                    is CameraRecordingUiAction.ChangeIsAudioEnabled -> onChangeIsAudioEnabled(action.enabled)
                    is CameraRecordingUiAction.StartStopRecording -> onStartStopRecording(action.newCapture)
                    is CameraRecordingUiAction.CheckPermissionClick -> onCheckPermission()
                    is CameraRecordingUiAction.UpdatePermissionGrantState -> updatePermissionGrantState(action.state)
                    is CameraRecordingUiAction.UpdateCameraPermissionGrantState -> updateCameraPermissionGrantState(action.state)
                    is CameraRecordingUiAction.UpdateAudioPermissionGrantState -> updateAudioPermissionGrantState(action.state)
                }
            }
        }
    }
}
