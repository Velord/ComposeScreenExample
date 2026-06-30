package com.velord.usecase.recording

import androidx.camera.video.VideoRecordEvent

fun interface DeleteFailedRecordingOutputUC : (VideoRecordEvent.Finalize) -> Unit
