package com.velord.data.gateway.camera

import com.velord.data.appstate.AppStateDataSource
import com.velord.model.camera.CameraRecordingState
import com.velord.model.camera.CameraSessionWrapper
import com.velord.model.camera.CameraState
import com.velord.model.camera.CameraVideoAsset
import com.velord.model.camera.config.CameraLens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import com.kashif.cameraK.enums.CameraLens as KameraLens
import com.kashif.cameraK.state.CameraKEvent as KameraEvent
import com.kashif.cameraK.state.CameraKState as KameraState
import com.kashif.cameraK.state.CameraUIState as KameraUiState
import com.kashif.cameraK.video.VideoCaptureResult as KameraVideoCaptureResult

@Single
class CameraStateGateway(private val appState: AppStateDataSource) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        observe()
    }

    fun getState(): StateFlow<CameraState> = appState.cameraStateFlow

    fun getLastVideoAsset(): StateFlow<CameraVideoAsset?> = appState.lastCameraVideoAssetFlow

    private fun observe() {
        observeCameraState()
        observeCameraEvent()
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

    private fun observeCameraEvent() {
        scope.launch {
            appState.cameraSessionFlow.filterNotNull().collectLatest { session ->
                session.value.events.collect { event ->
                    handleCameraEvent(event)
                }
            }
        }
    }

    private fun handleCameraEvent(event: KameraEvent) {
        when (event) {
            is KameraEvent.RecordingStarted -> onRecordingStarted()
            is KameraEvent.RecordingStopped -> onRecordingStopped(event.result)
            else -> Unit
        }
    }

    private fun onRecordingStopped(result: KameraVideoCaptureResult) {
        val asset = result.toCameraVideoAsset() ?: return
        appState.lastCameraVideoAssetFlow.value = asset
    }

    private fun onRecordingStarted() {
        appState.lastCameraVideoAssetFlow.value = null
    }

    private fun KameraVideoCaptureResult.toCameraVideoAsset(): CameraVideoAsset? {
        val success = this as? KameraVideoCaptureResult.Success ?: return null

        return CameraVideoAsset(fullFilePath = success.filePath)
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
