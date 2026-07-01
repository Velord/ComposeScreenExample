package com.velord.model.camera

import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture

actual class VideoCaptureWrapper(val value: VideoCapture<Recorder>)