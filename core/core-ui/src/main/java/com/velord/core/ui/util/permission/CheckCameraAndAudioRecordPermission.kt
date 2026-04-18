package com.velord.core.ui.util.permission

import android.Manifest
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import co.touchlab.kermit.Logger
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.velord.core.ui.dialog.showGoToSettingsForCamera
import com.velord.core.ui.dialog.showGoToSettingsForMic
import com.velord.core.ui.util.ObserveSharedFlow
import com.velord.util.permission.AndroidPermissionState
import kotlinx.coroutines.flow.MutableSharedFlow

private val log = Logger.withTag("CheckCameraAndAudioRecordPermission")

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckCameraAndAudioRecordPermission(
    triggerCheckEvent: MutableSharedFlow<Unit>,
    onCameraUpdateState: (AndroidPermissionState) -> Unit,
    onMicroUpdateState: (AndroidPermissionState) -> Unit,
) {
    val context = LocalContext.current
    // Fix the issue when the user first time asked for permission.
    // Can't do anything if user just leaves\close permission dialog infinite times.
    val permissionAlreadyRequestedState = rememberSaveable {
        mutableStateOf(false)
    }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        ),
        onPermissionsResult = { _ ->
            log.d { "onPermissionsResult" }
            if (permissionAlreadyRequestedState.value.not()) {
                permissionAlreadyRequestedState.value = true
            }
        }
    )

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

    LaunchedEffect(permissionsState) {
        log.d { "LaunchedEffect permissionsState" }
        permissionsState.launchMultiplePermissionRequest()
    }

    LaunchedEffect(permissionAlreadyRequestedState.value) {
        if (permissionAlreadyRequestedState.value.not()) return@LaunchedEffect

        log.d { "LaunchedEffect permissionAlreadyRequestedState" }
        checkCamera(permissionAlreadyRequestedState, cameraState, context)
        checkAudioRecord(permissionAlreadyRequestedState, microState, context)
    }

    cameraState.value?.let {
        log.d { "Camera: ${it.status}" }
        val androidPermState = it.status.toAndroidPermissionState(permissionAlreadyRequestedState.value)
        onCameraUpdateState(androidPermState)
    }
    microState.value?.let {
        log.d { "Micro: ${it.status}" }
        val androidPermState = it.status.toAndroidPermissionState(permissionAlreadyRequestedState.value)
        onMicroUpdateState(androidPermState)
    }

    ObserveSharedFlow(flow = triggerCheckEvent) {
        log.d { "ObserveTrigger != null" }
        permissionsState.launchMultiplePermissionRequest()

        checkCamera(permissionAlreadyRequestedState, cameraState, context)
        checkAudioRecord(permissionAlreadyRequestedState, microState, context)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun checkCamera(
    permissionAlreadyRequestedState: State<Boolean>,
    cameraState: State<PermissionState?>,
    context: Context
) {
    baseCheck(
        permissionAlreadyRequestedState,
        cameraState,
        { context.showGoToSettingsForCamera {} },
        "Camera"
    )
}

@OptIn(ExperimentalPermissionsApi::class)
private fun checkAudioRecord(
    permissionAlreadyRequestedState: State<Boolean>,
    microState: State<PermissionState?>,
    context: Context
) {
    baseCheck(
        permissionAlreadyRequestedState,
        microState,
        { context.showGoToSettingsForMic {} },
        "AudioRecord"
    )
}

@OptIn(ExperimentalPermissionsApi::class)
private fun baseCheck(
    permissionAlreadyRequestedState: State<Boolean>,
    permState: State<PermissionState?>,
    onCompletelyDenied: () -> Unit,
    tag: String
) {
    permState.value?.let {
        val isNotGranted = it.status.isGranted.not()
        val isNotShowRationale = it.status.shouldShowRationale.not()
        val isRequestedBefore = permissionAlreadyRequestedState.value
        log.d { "$tag: ${it.status.toAndroidPermissionState(isRequestedBefore)}" }
        if (isNotGranted && isNotShowRationale && isRequestedBefore) {
            onCompletelyDenied()
        }
    }
}
