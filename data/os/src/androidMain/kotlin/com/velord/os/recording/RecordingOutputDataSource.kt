package com.velord.os.recording

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.camera.video.FileDescriptorOutputOptions
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.VideoRecordEvent
import com.velord.core.resource.R
import com.velord.model.file.FileName
import org.koin.core.annotation.Single
import java.io.File

@Single
class RecordingOutputDataSource(
    private val context: Context,
) {

    fun createFileOutputOptions(fileName: FileName): FileOutputOptions {
        val moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        val appDir = File(moviesDir, context.getString(R.string.app_name)).apply {
            mkdirs()
        }
        val outputFile = File(appDir, fileName.value)
        return FileOutputOptions.Builder(outputFile).build()
    }

    fun deleteFailedOutput(finalizeEvent: VideoRecordEvent.Finalize) {
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
