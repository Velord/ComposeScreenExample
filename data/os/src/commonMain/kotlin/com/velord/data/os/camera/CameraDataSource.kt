package com.velord.data.os.camera

import com.velord.model.camera.CameraSessionWrapper
import com.velord.model.camera.CameraVideoRecordingRequest
import com.velord.model.camera.config.CameraLens
import com.velord.model.camera.config.CameraVideoQuality
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single
import com.kashif.cameraK.enums.CameraLens as KameraLens
import com.kashif.cameraK.state.CameraConfiguration as KameraConfiguration
import com.kashif.cameraK.state.CameraKStateHolder as KameraStateHolder
import com.kashif.cameraK.video.VideoConfiguration as KameraVideoConfiguration
import com.kashif.cameraK.video.VideoQuality as KameraVideoQuality

interface CameraDataSource {
    suspend fun createSession(): CameraSessionWrapper
    fun startRecording(request: CameraVideoRecordingRequest)
    fun stopRecording(session: CameraSessionWrapper)
    fun pauseRecording(session: CameraSessionWrapper)
    fun resumeRecording(session: CameraSessionWrapper)
    suspend fun toggleCameraLens(request: CameraVideoRecordingRequest): Boolean
    fun releaseSession(session: CameraSessionWrapper)
}

@Single(binds = [CameraDataSource::class])
class CameraDataSourceImpl(
    private val controllerFactory: CameraControllerFactory,
) : CameraDataSource {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override suspend fun createSession(): CameraSessionWrapper {
        val config = KameraConfiguration(cameraLens = KameraLens.FRONT)
        val stateHolder = KameraStateHolder(
            cameraConfiguration = config,
            controllerFactory = { controllerFactory.create(config) },
            coroutineScope = scope,
        )
        stateHolder.initialize()
        return CameraSessionWrapper(stateHolder)
    }

    override fun startRecording(request: CameraVideoRecordingRequest) {
        request.session.value.startRecording(
            KameraVideoConfiguration(
                quality = request.config.quality.toKameraVideoQuality(),
                enableAudio = request.config.audioConfig.isEnabled,
                outputDirectory = request.outputDirectory,
                filePrefix = request.filePrefix.value,
            ),
        )
    }

    override fun stopRecording(session: CameraSessionWrapper) {
        session.value.stopRecording()
    }

    override fun pauseRecording(session: CameraSessionWrapper) {
        session.value.pauseRecording()
    }

    override fun resumeRecording(session: CameraSessionWrapper) {
        session.value.resumeRecording()
    }

    override suspend fun toggleCameraLens(request: CameraVideoRecordingRequest): Boolean {
        val stateHolder = request.session.value
        if (stateHolder.uiState.value.cameraLens.toCameraLens() == request.config.lens) {
            return false
        }

        val shouldRestartRecording = stateHolder.uiState.value.isRecording
        if (shouldRestartRecording) {
            stateHolder.stopRecording()
            stateHolder.uiState.first { state -> state.isRecording.not() }
        }

        stateHolder.toggleCameraLens()
        if (shouldRestartRecording) {
            startRecording(request)
        }

        return shouldRestartRecording
    }

    override fun releaseSession(session: CameraSessionWrapper) {
        session.value.shutdown()
    }

    private fun KameraLens?.toCameraLens(): CameraLens = when (this) {
        KameraLens.BACK -> CameraLens.Back
        KameraLens.FRONT, null -> CameraLens.Front
    }

    private fun CameraVideoQuality.toKameraVideoQuality(): KameraVideoQuality = when (this) {
        CameraVideoQuality.Sd -> KameraVideoQuality.SD
        CameraVideoQuality.Hd -> KameraVideoQuality.HD
        CameraVideoQuality.FullHd -> KameraVideoQuality.FHD
        CameraVideoQuality.UltraHd -> KameraVideoQuality.UHD
    }
}
