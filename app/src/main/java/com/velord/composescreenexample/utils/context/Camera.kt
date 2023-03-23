package com.velord.composescreenexample.utils.context

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import com.velord.composescreenexample.ui.main.bottomNav.RecordVideoMetaData
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val MP4_EXT = ".mp4"

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
    fileMetaData: RecordVideoMetaData,
    videoCapture: VideoCapture<Recorder>,
    audioEnabled: Boolean,
    consumer: Consumer<VideoRecordEvent>
): Recording {
    val sdf = SimpleDateFormat(fileMetaData.fileNameFormat, Locale.US).format(System.currentTimeMillis()) + MP4_EXT
    val videoFile = File(fileMetaData.outputDirectory, sdf)
    val outputOptions = FileOutputOptions.Builder(videoFile).build()

    return videoCapture.output
        .prepareRecording(this, outputOptions)
        .apply { if (audioEnabled) withAudioEnabled() }
        .start(mainExecutor, consumer)
}

@SuppressLint("MissingPermission")
fun Context.createRecordingViaMediaStore(
    videoCapture: VideoCapture<Recorder>,
    audioEnabled: Boolean,
    consumer: Consumer<VideoRecordEvent>
): Recording {
    val name = System.currentTimeMillis().toString() + MP4_EXT
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
        put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video-Test")
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