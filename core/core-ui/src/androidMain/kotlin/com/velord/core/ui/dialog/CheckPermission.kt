package com.velord.core.ui.dialog

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.velord.core.resource.AppString
import com.velord.core.resource.AppStringResource
import com.velord.core.resource.getString
import com.velord.infrastructure.util.context.createSettingsIntent
import com.velord.model.localization.LocalizationState

private fun Context.askUserActivatePermissionInSettings(
    localization: LocalizationState,
    title: AppStringResource,
    message: AppStringResource,
    onDecline: () -> Unit,
) {
    alertDialog(
        title = getString(localization, title),
        message = getString(localization, message),
        positiveText = getString(localization, AppString.go_to_app_settings),
        negativeText = getString(localization, AppString.decline),
        positiveCallback = {
            startActivity(createSettingsIntent())
        },
        negativeCallback = onDecline,
        cancelable = false,
    )
}

fun Context.showGoToSettingsForMic(
    localization: LocalizationState,
    onDecline: () -> Unit,
) {
    askUserActivatePermissionInSettings(
        localization = localization,
        title = AppString.require_microphone_permission,
        message = AppString.give_access_to_microphone,
        onDecline = onDecline,
    )
}

fun Context.showGoToSettingsForCamera(
    localization: LocalizationState,
    onDecline: () -> Unit,
) {
    askUserActivatePermissionInSettings(
        localization = localization,
        title = AppString.require_camera_permission,
        message = AppString.give_access_to_camera,
        onDecline = onDecline,
    )
}

fun Fragment.checkRecordAudioPermission(
    localization: LocalizationState,
    actionLauncher: ActivityResultLauncher<String>,
    onGranted: () -> Unit,
) {
    val permRecordAudio = Manifest.permission.RECORD_AUDIO
    val isGranted = ContextCompat.checkSelfPermission(
        requireContext(),
        permRecordAudio,
    ) == PackageManager.PERMISSION_GRANTED

    when {
        isGranted -> onGranted()
        shouldShowRequestPermissionRationale(permRecordAudio) ->
            requireContext().showGoToSettingsForMic(localization) {}
        else -> actionLauncher.launch(permRecordAudio)
    }
}

fun Fragment.checkRecordVideoPermission(
    localization: LocalizationState,
    actionLauncher: ActivityResultLauncher<Array<String>>,
    onGranted: () -> Unit,
    onDecline: () -> Unit,
) {
    val permRecordAudio = Manifest.permission.RECORD_AUDIO
    val permCamera = Manifest.permission.CAMERA
    val permissionRoster = arrayOf(
        permRecordAudio,
        permCamera,
    )

    val isGranted = permissionRoster.all { permission ->
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission,
        ) == PackageManager.PERMISSION_GRANTED
    }

    when {
        isGranted -> onGranted()
        shouldShowRequestPermissionRationale(permRecordAudio) ->
            requireContext().showGoToSettingsForMic(localization, onDecline)
        shouldShowRequestPermissionRationale(permCamera) ->
            requireContext().showGoToSettingsForCamera(localization, onDecline)
        else -> actionLauncher.launch(permissionRoster)
    }
}
