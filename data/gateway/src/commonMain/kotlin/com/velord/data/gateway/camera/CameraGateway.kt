package com.velord.data.gateway.camera

import com.velord.data.appstate.AppStateDataSource
import com.velord.data.os.camera.CameraDataSource
import com.velord.model.camera.CameraLens
import com.velord.model.camera.CameraRecordingConfig
import com.velord.model.camera.CameraRecordingState
import com.velord.model.camera.CameraSessionWrapper
import com.velord.model.camera.CameraState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.annotation.Single
import com.kashif.cameraK.enums.CameraLens as KameraLens
import com.kashif.cameraK.state.CameraKState as KameraState
import com.kashif.cameraK.state.CameraUIState as KameraUiState

@Single
class CameraGateway(
    private val cameraDataSource: CameraDataSource,
    private val appState: AppStateDataSource,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val sessionMutex = Mutex()

    init {
        observeCameraState()
    }

    fun getSession(): StateFlow<CameraSessionWrapper?> = appState.cameraSessionFlow.asStateFlow()

    fun getState(): StateFlow<CameraState> = appState.cameraStateFlow.asStateFlow()

    suspend fun createSession() = sessionMutex.withLock {
        if (appState.cameraSessionFlow.value != null) return@withLock

        val session = cameraDataSource.createSession()
        appState.cameraSessionFlow.value = session
    }

    fun startRecording(config: CameraRecordingConfig) {
        appState.cameraSessionFlow.value?.let { session ->
            cameraDataSource.startRecording(session, config)
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
            cameraDataSource.toggleCameraLens(session, config)
        } ?: false

    suspend fun releaseSession() = sessionMutex.withLock {
        val session = appState.cameraSessionFlow.value ?: return@withLock
        cameraDataSource.releaseSession(session)
        appState.cameraSessionFlow.value = null
        appState.cameraStateFlow.value = CameraState.DEFAULT
    }

    private fun observeCameraState() {
        scope.launch {
            appState.cameraSessionFlow.filterNotNull().collectLatest { session ->
                session.toCameraStateFlow().collect { newState ->
                    appState.cameraStateFlow.value = newState
                }
            }
        }
    }

    private fun CameraSessionWrapper.toCameraStateFlow(): Flow<CameraState> = combine(
        value.cameraState,
        value.uiState,
    ) { cameraState, uiState ->
        CameraState(
            isReady = cameraState is KameraState.Ready,
            lens = uiState.cameraLens.toCameraLens(),
            recordingState = uiState.toCameraRecordingState(),
            recordingDurationMillis = uiState.recordingDurationMs,
            errorMessage = (cameraState as? KameraState.Error)?.message ?: uiState.lastError,
        )
    }

    private fun KameraLens?.toCameraLens(): CameraLens = when (this) {
        KameraLens.BACK -> CameraLens.Back
        KameraLens.FRONT, null -> CameraLens.Front
    }

    private fun KameraUiState.toCameraRecordingState(): CameraRecordingState = when {
        this.isPaused -> CameraRecordingState.Paused
        this.isRecording -> CameraRecordingState.Recording
        else -> CameraRecordingState.Idle
    }
}
