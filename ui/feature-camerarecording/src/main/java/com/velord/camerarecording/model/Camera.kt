package com.velord.camerarecording.model

import android.annotation.SuppressLint
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
suspend fun Context.createRecording(
    outputOptions: FileOutputOptions,
    videoCapture: VideoCapture<Recorder>,
    audioEnabled: Boolean,
    consumer: Consumer<VideoRecordEvent>
): Recording {
    return videoCapture.output
        .prepareRecording(this, outputOptions)
        .apply { if (audioEnabled) withAudioEnabled() }
        .start(mainExecutor, consumer)
}
