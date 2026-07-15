package com.velord.data.os.camera

import com.velord.model.camera.CameraRecordingConfig
import com.velord.model.camera.CameraSessionWrapper
import com.velord.model.camera.CameraVideoQuality
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope
import com.kashif.cameraK.enums.CameraLens as KameraLens
import com.kashif.cameraK.state.CameraConfiguration as KameraConfiguration
import com.kashif.cameraK.state.CameraKStateHolder as KameraStateHolder
import com.kashif.cameraK.video.VideoConfiguration as KameraVideoConfiguration
import com.kashif.cameraK.video.VideoQuality as KameraVideoQuality

interface CameraDataSource {
    suspend fun createSession(): CameraSessionWrapper
    fun startRecording(session: CameraSessionWrapper, config: CameraRecordingConfig)
    fun stopRecording(session: CameraSessionWrapper)
    fun pauseRecording(session: CameraSessionWrapper)
    fun resumeRecording(session: CameraSessionWrapper)
    suspend fun toggleCameraLens(
        session: CameraSessionWrapper,
        config: CameraRecordingConfig,
    ): Boolean
    fun releaseSession(session: CameraSessionWrapper)
}

@Module
expect class CameraPlatformModule() {
    @Single
    fun provideCameraControllerFactory(scope: Scope): CameraControllerFactory
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

    override fun startRecording(session: CameraSessionWrapper, config: CameraRecordingConfig) {
        session.value.startRecording(
            KameraVideoConfiguration(
                quality = config.quality.toVideoQuality(),
                enableAudio = config.isAudioEnabled,
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

    override suspend fun toggleCameraLens(
        session: CameraSessionWrapper,
        config: CameraRecordingConfig,
    ): Boolean {
        val stateHolder = session.value
        val shouldRestartRecording = stateHolder.uiState.value.isRecording
        if (shouldRestartRecording) {
            stateHolder.stopRecording()
            stateHolder.uiState.first { state -> state.isRecording.not() }
        }

        stateHolder.toggleCameraLens()
        if (shouldRestartRecording) startRecording(session, config)

        return shouldRestartRecording
    }

    override fun releaseSession(session: CameraSessionWrapper) {
        session.value.shutdown()
    }
}

private fun CameraVideoQuality.toVideoQuality(): KameraVideoQuality = when (this) {
    CameraVideoQuality.Sd -> KameraVideoQuality.SD
    CameraVideoQuality.Hd -> KameraVideoQuality.HD
    CameraVideoQuality.FullHd -> KameraVideoQuality.FHD
    CameraVideoQuality.UltraHd -> KameraVideoQuality.UHD
}
