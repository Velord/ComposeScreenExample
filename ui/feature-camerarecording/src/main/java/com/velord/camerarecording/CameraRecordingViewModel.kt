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
import com.velord.navigation.NavigationDataJetpack
import com.velord.navigation.NavigationDataVoyager
import com.velord.navigation.SharedScreen
import com.velord.navigation.entryPoint.SETTINGS_SOURCE
import com.velord.navigation.entryPoint.SettingsSource
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.util.file.FileName
import com.velord.util.permission.AndroidPermissionState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

interface CameraRecordingNavigator {
    fun goToSettingsFromCameraRecording()
}

class CameraRecordingViewModel(
    private val context: Context,
) : CoroutineScopeViewModel() {
    // Permission
    val permissionCameraFlow = MutableStateFlow(AndroidPermissionState.NotAsked)
    val permissionAudioFlow = MutableStateFlow(AndroidPermissionState.NotAsked)
    // User interaction
    val checkPermissionEvent = MutableSharedFlow<Unit>()
    val navigationEventVoyager = MutableSharedFlow<NavigationDataVoyager?>()
    val navigationEventJetpack = MutableSharedFlow<NavigationDataJetpack?>()
    val navigationEventDestination = MutableSharedFlow<Unit>()
    // Adjustments
    val videoQualityFlow = MutableStateFlow(Quality.HIGHEST)
    val videoCameraSelectorFlow = MutableStateFlow(CameraSelector.DEFAULT_FRONT_CAMERA)
    val isAudioEnabledFlow = MutableStateFlow(true)
    val isRecordingStartedFlow = MutableStateFlow(false)
    // Video control
    // A Recording is an object that allows us to control current active recording.
    // It will allow us to stop, pause and resume the current recording.
    // We create that object when we start recording.
    val recordingFlow: MutableStateFlow<Recording?> = MutableStateFlow(null)

    fun onSettingsClick() {
        launch {
            val nav = NavigationDataVoyager(
                screen = SharedScreen.BottomNavigationTab.Settings,
                useRoot = true
            )
            navigationEventVoyager.emit(nav)

            val bundle = bundleOf(SETTINGS_SOURCE to SettingsSource.CameraRecording)
            val data = NavigationDataJetpack(com.velord.resource.R.id.from_cameraRecordingFragment_to_settingsFragment, bundle)
            navigationEventJetpack.emit(data)
            navigationEventDestination.emit(Unit)
        }
    }

    fun updatePermissionState(state: AndroidPermissionState) {
        updateCameraPermissionState(state)
        updateAudioPermissionState(state)
    }

    fun updateCameraPermissionState(state: AndroidPermissionState) {
        permissionCameraFlow.value = state
    }

    fun updateAudioPermissionState(state: AndroidPermissionState) {
        permissionAudioFlow.value = state
    }

    fun onCheckPermission() {
        launch {
            checkPermissionEvent.emit(Unit)
        }
    }

    fun onChangeVideoQuality(quality: Quality) {
        videoQualityFlow.value = quality
    }

    fun onChangeVideoCameraSelector() {
        val isBackCamera = videoCameraSelectorFlow.value == CameraSelector.DEFAULT_BACK_CAMERA
        val newCamera = if (isBackCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        videoCameraSelectorFlow.value = newCamera
    }

    fun onChangeIsAudioEnabled(enabled: Boolean) {
        isAudioEnabledFlow.value = enabled
    }

    fun onStartStopRecording(newCapture: VideoCapture<Recorder>?) {
        if (isRecordingStartedFlow.value) {
            isRecordingStartedFlow.value = false
            onStopRecording()
        } else {
            newCapture?.let {
                isRecordingStartedFlow.value = true
                onNewRecording(it)
            }
        }
    }

    private fun onNewRecording(newCapture: VideoCapture<Recorder>) {
        val newRecording = context.createRecordingViaFileSystem(
            fileName = FileName(),
            videoCapture = newCapture,
            audioEnabled = isAudioEnabledFlow.value,
            consumer = ::onVideoRecordEvent,
        )
        recordingFlow.value = newRecording
    }

    private fun onStopRecording() {
        recordingFlow.value?.stop()
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
}