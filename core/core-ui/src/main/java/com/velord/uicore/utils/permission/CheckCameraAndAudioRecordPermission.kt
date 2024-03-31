package com.velord.uicore.utils.permission

import android.Manifest
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.velord.uicore.dialog.showRationalePermissionForCamera
import com.velord.uicore.dialog.showRationalePermissionForMic
import com.velord.uicore.utils.simulateTriggerStateAsSharedFlow
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckCameraAndAudioRecordPermission(
    triggerCheckEvent: Flow<Unit?>,
    onCameraUpdateState: (PermissionState) -> Unit,
    onMicroUpdateState: (PermissionState) -> Unit,
) {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
    )
    Log.d("CheckCameraAndAudioRecordPermission", "permissionsState: ${permissionsState}")

    SideEffect {
        Log.d("CheckCameraAndAudioRecordPermission", "SideEffect")
        permissionsState.launchMultiplePermissionRequest()
    }

    val cameraState = remember {
        derivedStateOf {
            permissionsState
                .permissions
                .firstOrNull { it.permission == Manifest.permission.CAMERA }
        }
    }
    val microState = remember {
        derivedStateOf {
            permissionsState
                .permissions
                .firstOrNull { it.permission == Manifest.permission.RECORD_AUDIO }
        }
    }
    cameraState.value?.let {
        Log.d("CheckCameraAndAudioRecordPermission", "Camera: ${it.status}")
        onCameraUpdateState(it)
    }
    microState.value?.let {
        Log.d("CheckCameraAndAudioRecordPermission", "Micro: ${it.status}")
        onMicroUpdateState(it)
    }

    val context = LocalContext.current
    ObserveTrigger(triggerCheckEvent = triggerCheckEvent) {
        Log.d("CheckCameraAndAudioRecordPermission", "ObserveTrigger != null")
        permissionsState.launchMultiplePermissionRequest()
        cameraState.value?.let {
            Log.d("CheckCameraAndAudioRecordPermission", "AndroidCamera: ${it.status}")
            if (it.status.isGranted.not() && it.status.shouldShowRationale.not()) {
                context.showRationalePermissionForCamera {}
            }
        }

        microState.value?.let {
            Log.d("CheckCameraAndAudioRecordPermission", "AndroidMicro: ${it.status}")
            if (it.status.isGranted.not() && it.status.shouldShowRationale.not()) {
                context.showRationalePermissionForMic {}
            }
        }
    }
}

@Composable
private fun ObserveTrigger(
    triggerCheckEvent: Flow<Unit?>,
    ons: () -> Unit
) {
    val checkPermissionTriggerState = triggerCheckEvent.simulateTriggerStateAsSharedFlow(before = {})
    if (checkPermissionTriggerState.value != null) {
        ons()
    }
}