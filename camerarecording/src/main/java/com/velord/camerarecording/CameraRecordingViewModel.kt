package com.velord.camerarecording

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.video.*
import com.velord.camerarecording.model.createRecordingViaMediaStore
import com.velord.composescreenexample.shared.navigation.NavigationData
import com.velord.model.file.FileName
import com.velord.util.permission.PermissionState
import com.velord.util.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraRecordingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : BaseViewModel() {
    // Permission
    val permissionFlow = MutableStateFlow(PermissionState.NotAsked)
    // User interaction
    val goToSettingsEvent = MutableSharedFlow<Unit>()
    val checkPermissionEvent = MutableSharedFlow<Unit>()
    val navigationEvent = MutableSharedFlow<NavigationData>()
    // Adjustments
    val videoQualityFlow = MutableStateFlow(Quality.HIGHEST)
    val videoCameraSelectorFlow = MutableStateFlow(CameraSelector.DEFAULT_FRONT_CAMERA)
    val videoIsAudioEnabledFlow = MutableStateFlow(true)
    // Video control
    // A Recording is an object that allows us to control current active recording.
    // It will allow us to stop, pause and resume the current recording.
    // We create that object when we start recording.
    val recordingFlow: MutableStateFlow<Recording?> = MutableStateFlow(null)

    fun onSettingsClick() = launch {
        val nav = NavigationData(
            R.id.from_cameraRecordingFragment_to_settingsFragment,
            null
        )
        navigationEvent.emit(nav)
    }

    fun onGoToSettingsClick() = launch {
        goToSettingsEvent.emit(Unit)
    }

    fun updatePermissionState(state: PermissionState) {
        permissionFlow.value = state
    }


    fun onCheckPermission() = launch {
        checkPermissionEvent.emit(Unit)
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

    fun onChangeVideoIsAudioEnabled(enabled: Boolean) {
        videoIsAudioEnabledFlow.value = enabled
    }

    fun onNewRecording(newCapture: VideoCapture<Recorder>) {
        val newRecording = context.createRecordingViaMediaStore(
            fileName = FileName(),
            videoCapture = newCapture,
            audioEnabled = videoIsAudioEnabledFlow.value,
            consumer = ::onVideoRecordEvent,
        )
        recordingFlow.value = newRecording
    }

    fun onStopRecording() {
        recordingFlow.value?.stop()
    }

    private fun onVideoRecordEvent(newEvent: VideoRecordEvent) {
        if (newEvent is VideoRecordEvent.Finalize) {
            val isNone = newEvent.error == VideoRecordEvent.Finalize.ERROR_NONE
            if (isNone.not()) {
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
}