package com.velord.composescreenexample.ui.main.bottomNav

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.video.*
import com.velord.composescreenexample.utils.shared.PermissionState
import com.velord.composescreenexample.utils.context.createDirInCache
import com.velord.composescreenexample.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

private const val DIR_NAME = "video"
private const val VIDEO_FILE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

data class RecordVideoMetaData(
    val outputDirectory: File,
    val fileNameFormat: String,
)

@HiltViewModel
class CameraRecordingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : BaseViewModel() {
    // Permission
    val permissionFlow = MutableStateFlow(PermissionState.NotAsked)
    // User interaction
    val goToSettingsEvent = MutableSharedFlow<Unit>()
    val checkPermissionEvent = MutableSharedFlow<Unit>()
    // Adjustments
    val videoQualityFlow = MutableStateFlow(Quality.HIGHEST)
    val videoCameraSelectorFlow = MutableStateFlow(CameraSelector.DEFAULT_FRONT_CAMERA)
    val videoIsAudioEnabledFlow = MutableStateFlow(true)
    // Video control
    // A Recording is an object that allows us to control current active recording.
    // It will allow us to stop, pause and resume the current recording.
    // We create that object when we start recording.
    val recordingFlow: MutableStateFlow<Recording?> = MutableStateFlow(null)

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

    fun onNewRecording(recording: Recording) {
        recordingFlow.value = recording
    }

    fun onStopRecording() {
        recordingFlow.value?.stop()
    }

    fun onVideoRecordEvent(newEvent: VideoRecordEvent) {
        Log.d("CameraRecordingViewModel", "Recording event: $newEvent")
        if (newEvent is VideoRecordEvent.Finalize) {
            val uri = newEvent.outputResults.outputUri
            //if (uri != Uri.EMPTY) saveVideo(uri)
            Log.d("CameraRecordingViewModel", "Recording uri: $uri")
        }
    }

    fun createFileMetadata(): RecordVideoMetaData {
        val mediaDir = context.createDirInCache(DIR_NAME)
        val dir = if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
        return RecordVideoMetaData(
            outputDirectory = dir,
            fileNameFormat = VIDEO_FILE_FORMAT,
        )
    }

    private fun saveVideoToGallery(uri: Uri) {
        val values = ContentValues(1).apply {
            put(MediaStore.Video.Media.TITLE, "My video title")
            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.DATA, uri.path)
        }
        val newUri = context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)

    }
}