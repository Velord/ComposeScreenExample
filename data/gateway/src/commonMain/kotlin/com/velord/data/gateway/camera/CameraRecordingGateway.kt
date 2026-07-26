package com.velord.data.gateway.camera

import com.velord.data.appstate.AppStateDataSource
import com.velord.data.os.camera.CameraDataSource
import com.velord.data.os.file.FileDataSource
import com.velord.data.os.file.FileDirectory
import com.velord.data.os.file.FileStorageScope
import com.velord.model.camera.CameraSessionWrapper
import com.velord.model.camera.CameraVideoRecordingRequest
import com.velord.model.camera.config.CameraRecordingConfig
import com.velord.model.file.FileName
import org.koin.core.annotation.Single

@Single
class CameraRecordingGateway(
    private val cameraDataSource: CameraDataSource,
    private val fileDataSource: FileDataSource,
    private val appState: AppStateDataSource,
) {

    fun startRecording(config: CameraRecordingConfig) {
        appState.cameraSessionFlow.value?.let { session ->
            cameraDataSource.startRecording(createRecordingRequest(session, config))
        }
    }

    fun stopRecording() {
        appState.cameraSessionFlow.value?.let(cameraDataSource::stopRecording)
    }

    fun pauseRecording() {
        appState.cameraSessionFlow.value?.let(cameraDataSource::pauseRecording)
    }

    fun resumeRecording() {
        appState.cameraSessionFlow.value?.let(cameraDataSource::resumeRecording)
    }

    suspend fun toggleCameraLens(config: CameraRecordingConfig): Boolean =
        appState.cameraSessionFlow.value?.let { session ->
            cameraDataSource.toggleCameraLens(createRecordingRequest(session, config))
        } ?: false

    private fun createRecordingRequest(
        session: CameraSessionWrapper,
        config: CameraRecordingConfig,
    ): CameraVideoRecordingRequest = CameraVideoRecordingRequest(
        session = session,
        config = config,
        outputDirectory = getVideoDirectoryPath(),
        filePrefix = FileName(extension = ""),
    )

    private fun getVideoDirectoryPath(): String = fileDataSource.getDirectoryPath(
        directory = FileDirectory.Video,
        storageScope = FileStorageScope.Public,
    )
}
