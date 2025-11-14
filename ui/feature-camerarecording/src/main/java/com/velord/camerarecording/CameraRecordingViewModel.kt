package com.velord.camerarecording

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.video.FileDescriptorOutputOptions
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.core.os.bundleOf
import com.velord.camerarecording.model.createRecordingViaFileSystem
import com.velord.core.resource.R
import com.velord.navigation.fragment.NavigationDataFragment
import com.velord.navigation.fragment.entryPoint.SETTINGS_SOURCE
import com.velord.navigation.fragment.entryPoint.SettingsSourceFragment
import com.velord.navigation.voyager.NavigationDataVoyager
import com.velord.navigation.voyager.SharedScreenVoyager
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.util.file.FileName
import com.velord.util.permission.AndroidPermissionState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface CameraRecordingNavigator {
    fun goToSettingFromCameraRecording()
}

data class CameraRecordingUiState(
    // Permission
    val permissionCamera: AndroidPermissionState,
    val permissionAudio: AndroidPermissionState,
    // Video control
    val videoQuality: Quality,
    val cameraSelector: CameraSelector,
    val isAudioEnabled: Boolean,
    val isRecordingStarted: Boolean,
    // A Recording is an object that allows us to control current active recording.
    // It will allow us to stop, pause and resume the current recording.
    // We create that object when we start recording.
    val recording: Recording?,
) {
    companion object {
        val DEFAULT = CameraRecordingUiState(
            permissionCamera = AndroidPermissionState.NotAsked,
            permissionAudio = AndroidPermissionState.NotAsked,
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
    data class UpdateCameraPermissionState(val state: AndroidPermissionState) : CameraRecordingUiAction
    data class UpdateAudioPermissionState(val state: AndroidPermissionState) : CameraRecordingUiAction
    data class UpdatePermissionState(val state: AndroidPermissionState) : CameraRecordingUiAction
}

class CameraRecordingViewModel(
    private val context: Context,
) : CoroutineScopeViewModel() {

    val uiStateFlow = MutableStateFlow(CameraRecordingUiState.DEFAULT)
    // User interaction
    val checkPermissionEvent = MutableSharedFlow<Unit>()
    val navigationEventVoyager = MutableSharedFlow<NavigationDataVoyager?>()
    val navigationEventJetpack = MutableSharedFlow<NavigationDataFragment?>()
    val navigationEventDestination = MutableSharedFlow<CameraRecordingNavigationEvent>()
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

            val bundle = bundleOf(SETTINGS_SOURCE to SettingsSourceFragment.CameraRecording)
            val data = NavigationDataFragment(R.id.from_cameraRecordingFragment_to_settingsFragment, bundle)
            navigationEventJetpack.emit(data)
            navigationEventDestination.emit(CameraRecordingNavigationEvent.SETTINGS)
        }
    }

    private fun updatePermissionState(state: AndroidPermissionState) {
        updateCameraPermissionState(state)
        updateAudioPermissionState(state)
    }

    private fun updateCameraPermissionState(state: AndroidPermissionState) {
        uiStateFlow.update {
            it.copy(permissionCamera = state)
        }
    }

    private fun updateAudioPermissionState(state: AndroidPermissionState) {
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
        val isAudioEnabled = uiStateFlow.value.isAudioEnabled
        val newRecording = context.createRecordingViaFileSystem(
            fileName = FileName(),
            videoCapture = newCapture,
            audioEnabled = isAudioEnabled,
            consumer = ::onVideoRecordEvent,
        )
        uiStateFlow.update {
            it.copy(recording = newRecording)
        }
    }

    private fun onStopRecording() {
        uiStateFlow.value.recording?.stop()
    }

    private fun onVideoRecordEvent(newEvent: VideoRecordEvent) {
        if (newEvent is VideoRecordEvent.Finalize) {
            val isNone = newEvent.error == VideoRecordEvent.Finalize.ERROR_NONE
            if (isNone) return
            // When error delete the file
            when(val options = newEvent.outputOptions) {
                is FileOutputOptions -> options.file.delete()
                is MediaStoreOutputOptions -> {
                    val uri: Uri = newEvent.outputResults.outputUri
                    if (uri != Uri.EMPTY) {
                        context.contentResolver.delete(uri, null, null)
                    }
                }
                is FileDescriptorOutputOptions -> {
                    // User has to clean up the referenced target of the file descriptor.
                }
            }
        }
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
                    is CameraRecordingUiAction.UpdatePermissionState -> updatePermissionState(action.state)
                    is CameraRecordingUiAction.UpdateCameraPermissionState -> updateCameraPermissionState(action.state)
                    is CameraRecordingUiAction.UpdateAudioPermissionState -> updateAudioPermissionState(action.state)
                }
            }
        }
    }
}