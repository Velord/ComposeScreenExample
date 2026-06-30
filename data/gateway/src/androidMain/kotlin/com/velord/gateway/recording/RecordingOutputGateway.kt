package com.velord.gateway.recording

import androidx.camera.video.FileOutputOptions
import androidx.camera.video.VideoRecordEvent
import com.velord.model.file.FileName
import com.velord.os.recording.RecordingOutputDataSource
import org.koin.core.annotation.Single

@Single
class RecordingOutputGateway(
    private val dataSource: RecordingOutputDataSource,
) {

    fun createFileOutputOptions(fileName: FileName): FileOutputOptions =
        dataSource.createFileOutputOptions(fileName)

    fun deleteFailedOutput(finalizeEvent: VideoRecordEvent.Finalize) {
        dataSource.deleteFailedOutput(finalizeEvent)
    }
}
