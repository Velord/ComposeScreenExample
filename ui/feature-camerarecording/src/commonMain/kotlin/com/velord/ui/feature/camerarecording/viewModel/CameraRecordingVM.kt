package com.velord.ui.feature.camerarecording.viewModel

import com.velord.core.resource.Res
import com.velord.core.resource.recording_restarted_after_camera_switch
import com.velord.infrastructure.util.permission.PermissionGrantState
import com.velord.model.ToastConfig
import com.velord.model.ToastDuration
import com.velord.model.camera.CameraRecordingState
import com.velord.model.camera.config.CameraAudioConfig
import com.velord.model.camera.config.CameraLens
import com.velord.model.camera.config.CameraRecordingConfig
import com.velord.model.camera.config.CameraVideoQuality
import com.velord.ui.feature.camerarecording.CameraRecordingNavigationEvent
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import com.velord.usecase.camera.CreateCameraSessionUC
import com.velord.usecase.camera.GetCameraSessionUC
import com.velord.usecase.camera.GetCameraStateUC
import com.velord.usecase.camera.GetLastCameraVideoAssetUC
import com.velord.usecase.camera.OpenCameraVideoFolderUC
import com.velord.usecase.camera.StartRecordingUC
import com.velord.usecase.camera.StopRecordingUC
import com.velord.usecase.camera.ToggleCameraLensUC
import com.velord.usecase.event.ShowToastUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class CameraRecordingVM(
    private val createCameraSession: CreateCameraSessionUC,
    private val getCameraSession: GetCameraSessionUC,
    private val getCameraState: GetCameraStateUC,
    private val getLastCameraVideoAsset: GetLastCameraVideoAssetUC,
    private val startRecording: StartRecordingUC,
    private val stopRecording: StopRecordingUC,
    private val toggleCameraLens: ToggleCameraLensUC,
    private val openCameraVideoFolder: OpenCameraVideoFolderUC,
    private val showToastUC: ShowToastUC,
) : CoroutineScopeVM() {

    val uiStateFlow = MutableStateFlow(CameraRecordingUiState.DEFAULT)
    val checkPermissionEvent = MutableSharedFlow<Unit>()
    val navigationEvent = MutableSharedFlow<CameraRecordingNavigationEvent>()
    private val actionFlow = MutableSharedFlow<CameraRecordingUiAction>()

    init {
        observe()
    }

    fun onAction(action: CameraRecordingUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onSettingsClick() {
        launch {
            navigationEvent.emit(CameraRecordingNavigationEvent.Setting)
        }
    }

    private fun onCreateCameraSession() {
        launch { createCameraSession() }
    }

    private fun onUpdatePermissionGrantState(state: PermissionGrantState) {
        onUpdateCameraPermissionGrantState(state)
        onUpdateAudioPermissionGrantState(state)
    }

    private fun onUpdateCameraPermissionGrantState(state: PermissionGrantState) {
        uiStateFlow.update { uiState ->
            uiState.copy(permissionState = uiState.permissionState.copy(camera = state))
        }
    }

    private fun onUpdateAudioPermissionGrantState(state: PermissionGrantState) {
        uiStateFlow.update { uiState ->
            uiState.copy(permissionState = uiState.permissionState.copy(audio = state))
        }
    }

    private fun onCheckPermissionClick() {
        launch {
            checkPermissionEvent.emit(Unit)
        }
    }

    private fun onChangeVideoQuality(quality: CameraVideoQuality) {
        uiStateFlow.update { uiState ->
            uiState.copy(videoQuality = quality)
        }
    }

    private fun onChangeCameraSelector() = launch {
        val uiState = uiStateFlow.value
        val config = CameraRecordingConfig(
            lens = uiState.cameraState.lens.next(),
            quality = uiState.videoQuality,
            audioConfig = CameraAudioConfig(isEnabled = uiState.isAudioEnabled),
        )
        val wasRecordingRestarted = toggleCameraLens(config)
        if (wasRecordingRestarted) showRecordingRestartedWarning()
    }

    private suspend fun showRecordingRestartedWarning() {
        val message = getString(Res.string.recording_restarted_after_camera_switch)
        val toastConfig = ToastConfig(message = message, duration = ToastDuration.Long)
        showToastUC(toastConfig)
    }

    private fun onChangeIsAudioEnabled(enabled: Boolean) {
        uiStateFlow.update { uiState ->
            uiState.copy(isAudioEnabled = enabled)
        }
    }

    private fun onStartStopRecording() {
        val uiState = uiStateFlow.value
        if (uiState.cameraState.recordingState == CameraRecordingState.Idle) {
            startRecording(
                CameraRecordingConfig(
                    lens = uiState.cameraState.lens,
                    quality = uiState.videoQuality,
                    audioConfig = CameraAudioConfig(isEnabled = uiState.isAudioEnabled),
                ),
            )
        } else {
            stopRecording()
        }
    }

    private fun onOpenVideoFolder() {
        val directoryPath = uiStateFlow.value.lastVideoAsset?.directoryPath ?: return
        openCameraVideoFolder(directoryPath)
    }

    private fun handleUiAction(action: CameraRecordingUiAction) {
        when (action) {
            CameraRecordingUiAction.CreateCameraSession -> onCreateCameraSession()
            CameraRecordingUiAction.SettingsClick -> onSettingsClick()
            CameraRecordingUiAction.ChangeCameraSelector -> onChangeCameraSelector()
            CameraRecordingUiAction.CheckPermissionClick -> onCheckPermissionClick()
            CameraRecordingUiAction.StartStopRecording -> onStartStopRecording()
            CameraRecordingUiAction.OpenVideoFolder -> onOpenVideoFolder()
            is CameraRecordingUiAction.ChangeVideoQuality -> onChangeVideoQuality(action.quality)
            is CameraRecordingUiAction.ChangeIsAudioEnabled ->
                onChangeIsAudioEnabled(action.enabled)
            is CameraRecordingUiAction.UpdatePermissionGrantState ->
                onUpdatePermissionGrantState(action.state)
            is CameraRecordingUiAction.UpdateCameraPermissionGrantState ->
                onUpdateCameraPermissionGrantState(action.state)
            is CameraRecordingUiAction.UpdateAudioPermissionGrantState ->
                onUpdateAudioPermissionGrantState(action.state)
        }
    }

    private fun CameraLens.next(): CameraLens = when (this) {
        CameraLens.Front -> CameraLens.Back
        CameraLens.Back -> CameraLens.Front
    }

    private fun observe() {
        launch {
            getCameraSession().collect { session ->
                uiStateFlow.update { uiState ->
                    uiState.copy(cameraSession = session)
                }
            }
        }
        launch {
            getCameraState().collect { cameraState ->
                uiStateFlow.update { uiState ->
                    uiState.copy(cameraState = cameraState)
                }
            }
        }
        launch {
            getLastCameraVideoAsset().collect { asset ->
                uiStateFlow.update { uiState ->
                    uiState.copy(lastVideoAsset = asset)
                }
            }
        }
        launch {
            actionFlow.collect { action ->
                handleUiAction(action)
            }
        }
    }
}
