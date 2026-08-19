package com.velord.core.ui.permission

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
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.velord.core.resource.LocalLocalizationState
import com.velord.core.ui.dialog.showGoToSettingsForCamera
import com.velord.core.ui.dialog.showGoToSettingsForMic
import com.velord.core.ui.util.ObserveSharedFlow
import com.velord.infrastructure.util.permission.PermissionGrantState
import com.velord.infrastructure.util.persmission.toPermissionGrantState
import com.velord.model.localization.LocalizationState
import kotlinx.coroutines.flow.MutableSharedFlow
import com.google.accompanist.permissions.PermissionState as AccompanistPermissionState

private val log = Logger.withTag("CheckCameraAndAudioRecordPermission")

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckCameraAndAudioRecordPermission(
    triggerCheckEvent: MutableSharedFlow<Unit>,
    onCameraUpdateState: (PermissionGrantState) -> Unit,
    onMicroUpdateState: (PermissionGrantState) -> Unit,
) {
    val context = LocalContext.current
    val localization = LocalLocalizationState.current
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
            permissionsState.permissions.firstOrNull { it.permission == Manifest.permission.CAMERA }
        }
    }
    val microState = remember {
        derivedStateOf {
            permissionsState.permissions.firstOrNull { it.permission == Manifest.permission.RECORD_AUDIO }
        }
    }

    LaunchedEffect(permissionsState) {
        log.d { "LaunchedEffect permissionsState" }
        permissionsState.launchMultiplePermissionRequest()
    }

    LaunchedEffect(permissionAlreadyRequestedState.value, localization) {
        if (permissionAlreadyRequestedState.value.not()) return@LaunchedEffect
        val currentLocalization = localization ?: return@LaunchedEffect

        log.d { "LaunchedEffect permissionAlreadyRequestedState" }
        checkCamera(permissionAlreadyRequestedState, cameraState, context, currentLocalization)
        checkAudioRecord(permissionAlreadyRequestedState, microState, context, currentLocalization)
    }

    cameraState.value?.let {
        log.d { "Camera: ${it.status}" }
        val permissionGrantState = it.status.toPermissionGrantState(
            isRequestedBefore = permissionAlreadyRequestedState.value
        )
        onCameraUpdateState(permissionGrantState)
    }
    microState.value?.let {
        log.d { "Micro: ${it.status}" }
        val permissionGrantState = it.status.toPermissionGrantState(
            isRequestedBefore = permissionAlreadyRequestedState.value
        )
        onMicroUpdateState(permissionGrantState)
    }

    ObserveSharedFlow(flow = triggerCheckEvent) {
        log.d { "ObserveTrigger != null" }
        permissionsState.launchMultiplePermissionRequest()

        localization?.let { currentLocalization ->
            checkCamera(permissionAlreadyRequestedState, cameraState, context, currentLocalization)
            checkAudioRecord(permissionAlreadyRequestedState, microState, context, currentLocalization)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun checkCamera(
    permissionAlreadyRequestedState: State<Boolean>,
    cameraState: State<AccompanistPermissionState?>,
    context: Context,
    localization: LocalizationState,
) {
    baseCheck(
        permissionAlreadyRequestedState,
        cameraState,
        { context.showGoToSettingsForCamera(localization) {} },
        "Camera"
    )
}

@OptIn(ExperimentalPermissionsApi::class)
private fun checkAudioRecord(
    permissionAlreadyRequestedState: State<Boolean>,
    microState: State<AccompanistPermissionState?>,
    context: Context,
    localization: LocalizationState,
) {
    baseCheck(
        permissionAlreadyRequestedState,
        microState,
        { context.showGoToSettingsForMic(localization) {} },
        "AudioRecord"
    )
}

@OptIn(ExperimentalPermissionsApi::class)
private fun baseCheck(
    permissionAlreadyRequestedState: State<Boolean>,
    permState: State<AccompanistPermissionState?>,
    onCompletelyDenied: () -> Unit,
    tag: String
) {
    permState.value?.let {
        val isNotGranted = it.status.isGranted.not()
        val isNotShowRationale = it.status.shouldShowRationale.not()
        val isRequestedBefore = permissionAlreadyRequestedState.value
        log.d { "$tag: ${it.status.toPermissionGrantState(isRequestedBefore)}" }
        if (isNotGranted && isNotShowRationale && isRequestedBefore) {
            onCompletelyDenied()
        }
    }
}
