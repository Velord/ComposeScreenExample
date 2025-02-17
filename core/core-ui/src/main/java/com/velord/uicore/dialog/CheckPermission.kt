package com.velord.uicore.dialog

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.velord.resource.R
import com.velord.util.context.createSettingsIntent

private fun Context.askUserActivatePermissionInSettings(
    @StringRes title: Int,
    @StringRes message: Int,
    onDecline: () -> Unit
) {
    alertDialog(
        title = title,
        message = message,
        positiveText = R.string.go_to_app_settings,
        negativeText = R.string.decline,
        positiveCallback = {
            startActivity(createSettingsIntent())
        },
        negativeCallback = onDecline,
        cancelable = false
    )
}

fun Context.showRationalePermissionForMic(
    onDecline: () -> Unit
) {
    askUserActivatePermissionInSettings(
        title = R.string.require_microphone_permission,
        message = R.string.give_access_to_microphone,
        onDecline = onDecline
    )
}

fun Context.showRationalePermissionForCamera(
    onDecline: () -> Unit
) {
    askUserActivatePermissionInSettings(
        title = R.string.require_camera_permission,
        message = R.string.give_access_to_camera,
        onDecline = onDecline
    )
}

fun Fragment.checkRecordVideoPermission(
    actionLauncher: ActivityResultLauncher<Array<String>>,
    onGranted: () -> Unit,
    onDecline: () -> Unit
) {
    val permRecordAudio = Manifest.permission.RECORD_AUDIO
    val permCamera = Manifest.permission.CAMERA
    val permissions = arrayOf(
        permRecordAudio,
        permCamera
    )
    val isGranted = permissions.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            permRecordAudio
        ) == PackageManager.PERMISSION_GRANTED
    }

    when {
        isGranted -> onGranted()
        shouldShowRequestPermissionRationale(permRecordAudio) ->
            requireContext().showRationalePermissionForMic(onDecline)
        shouldShowRequestPermissionRationale(permCamera) ->
            requireContext().showRationalePermissionForCamera(onDecline)
        else -> actionLauncher.launch(permissions)
    }
}