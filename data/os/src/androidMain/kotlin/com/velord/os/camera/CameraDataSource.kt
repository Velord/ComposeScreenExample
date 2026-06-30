package com.velord.os.camera

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.camera.video.FileDescriptorOutputOptions
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.VideoRecordEvent
import com.velord.core.resource.R
import com.velord.model.camera.RecordingSession
import com.velord.model.camera.VideoCaptureRequest
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DEFAULT_EXTENSION = ".mp4"

@Module
actual class CameraPlatformModule {

    @Single
    actual fun provideCameraDataSource(scope: Scope): CameraDataSource =
        AndroidCameraDataSource(scope.get())
}

private class AndroidCameraDataSource(private val context: Context) : CameraDataSource {

    @SuppressLint("MissingPermission")
    override fun startRecording(
        videoCapture: VideoCaptureRequest,
        audioEnabled: Boolean,
    ): RecordingSession {
        val outputOptions = createFileOutputOptions()
        val recording = videoCapture.value.output
            .prepareRecording(context, outputOptions)
            .apply { if (audioEnabled) withAudioEnabled() }
            .start(context.mainExecutor, ::onVideoRecordEvent)
        return CameraXRecordingSession(recording)
    }

    private fun createFileOutputOptions(): FileOutputOptions {
        val moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        val appDir = File(moviesDir, context.getString(R.string.app_name)).apply {
            mkdirs()
        }
        val outputFile = File(appDir, createFileName())
        return FileOutputOptions.Builder(outputFile).build()
    }

    private fun createFileName(extension: String = DEFAULT_EXTENSION): String {
        val timestamp = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(Date())
        return "$timestamp$extension"
    }

    private fun onVideoRecordEvent(newEvent: VideoRecordEvent) {
        if (newEvent is VideoRecordEvent.Finalize) {
            val isNone = newEvent.error == VideoRecordEvent.Finalize.ERROR_NONE
            if (isNone) return
            deleteFailedOutput(newEvent)
        }
    }

    private fun deleteFailedOutput(finalizeEvent: VideoRecordEvent.Finalize) {
        when (val outputOptions = finalizeEvent.outputOptions) {
            is FileOutputOptions -> outputOptions.file.delete()
            is MediaStoreOutputOptions -> {
                val outputUri = finalizeEvent.outputResults.outputUri
                if (outputUri != Uri.EMPTY) {
                    context.contentResolver.delete(outputUri, null, null)
                }
            }
            is FileDescriptorOutputOptions -> Unit
        }
    }
}
