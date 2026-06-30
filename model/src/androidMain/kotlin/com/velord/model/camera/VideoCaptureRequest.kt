package com.velord.model.camera

import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture

actual class VideoCaptureRequest(val value: VideoCapture<Recorder>)