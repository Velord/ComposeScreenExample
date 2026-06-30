package com.velord.usecase.recording

import androidx.camera.video.FileOutputOptions
import com.velord.model.file.FileName

fun interface CreateRecordingFileOutputOptionsUC : (FileName) -> FileOutputOptions
