package com.velord.camerarecording.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import com.velord.resource.R
import com.velord.util.file.FileName
import com.velord.util.file.NewFile
import com.velord.util.file.OutputDirectory
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val MIME_TYPE = "video/mp4"
private const val FOLDER_MOVIES = "Movies/"

suspend fun Context.createVideoCapture(
    lifecycleOwner: LifecycleOwner,
    cameraSelector: CameraSelector,
    previewView: PreviewView,
    preferredQuality: Quality = Quality.HIGHEST
): VideoCapture<Recorder> {
    val preview = Preview.Builder()
        .build()
        .apply { setSurfaceProvider(previewView.surfaceProvider) }

    val qualitySelector = QualitySelector.from(
        preferredQuality,
        FallbackStrategy.lowerQualityOrHigherThan(Quality.FHD)
    )
    val recorder = Recorder.Builder()
        .setExecutor(mainExecutor)
        .setQualitySelector(qualitySelector)
        .build()
    val videoCapture = VideoCapture.withOutput(recorder)

    getCameraProvider().apply {
        unbindAll()
        bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            videoCapture
        )
    }

    return videoCapture
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener(
            {
                continuation.resume(future.get())
            },
            mainExecutor
        )
    }
}

@SuppressLint("MissingPermission")
fun Context.createRecordingViaFileSystem(
    fileName: FileName,
    videoCapture: VideoCapture<Recorder>,
    audioEnabled: Boolean,
    consumer: Consumer<VideoRecordEvent>
): Recording {
    val appDirName = getString(R.string.app_name)
    val moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
    val fullDirName = File(moviesDir, appDirName)
    val newFile = NewFile(
        dir = OutputDirectory(fullDirName),
        name = fileName
    )

    val outputOptions = FileOutputOptions.Builder(newFile.value).build()

    return videoCapture.output
        .prepareRecording(this, outputOptions)
        .apply { if (audioEnabled) withAudioEnabled() }
        .start(mainExecutor, consumer)
}

@SuppressLint("MissingPermission")
fun Context.createRecordingViaMediaStore(
    fileName: FileName,
    videoCapture: VideoCapture<Recorder>,
    audioEnabled: Boolean,
    consumer: Consumer<VideoRecordEvent>
): Recording {
    val appDirName = FOLDER_MOVIES + this.getString(R.string.app_name)
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName.value)
        put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE)
        put(MediaStore.Video.Media.RELATIVE_PATH, appDirName)
    }
    val mediaStoreOutputOptions = MediaStoreOutputOptions
        .Builder(this.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        .setContentValues(contentValues)
        .build()

    return videoCapture.output
        .prepareRecording(this, mediaStoreOutputOptions)
        .apply { if (audioEnabled) withAudioEnabled() }
        .start(mainExecutor, consumer)
}