package com.velord.uicore.dialog

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.velord.resource.R
import com.velord.util.context.createSettingsIntent

private fun Fragment.showRationalePermission(
    @StringRes title: Int,
    @StringRes message: Int,
    onDecline: () -> Unit
) {
    requireContext().alertDialog(
        title = title,
        message = message,
        positiveText = R.string.go_to_app_settings,
        negativeText = R.string.decline,
        positiveCallback = {
            startActivity(requireContext().createSettingsIntent())
        },
        negativeCallback = onDecline,
        cancelable = false
    )
}

private fun Fragment.showRationalePermissionForMic(
    onDecline: () -> Unit
) {
    showRationalePermission(
        title = R.string.require_microphone_permission,
        message = R.string.give_access_to_microphone,
        onDecline = onDecline
    )
}

private fun Fragment.showRationalePermissionForCamera(
    onDecline: () -> Unit
) {
    showRationalePermission(
        title = R.string.require_camera_permission,
        message = R.string.give_access_to_camera,
        onDecline = onDecline
    )
}

fun Fragment.checkRecordAudioPermission(
    actionLauncher: ActivityResultLauncher<String>,
    onGranted: () -> Unit
) {
    val permRecordAudio = Manifest.permission.RECORD_AUDIO
    val isGranted = ContextCompat.checkSelfPermission(
        requireContext(),
        permRecordAudio
    ) == PackageManager.PERMISSION_GRANTED

    when {
        isGranted -> onGranted()
        shouldShowRequestPermissionRationale(permRecordAudio) -> showRationalePermissionForMic {}
        else -> actionLauncher.launch(permRecordAudio)
    }
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
            showRationalePermissionForMic(onDecline)
        shouldShowRequestPermissionRationale(permCamera) ->
            showRationalePermissionForCamera(onDecline)
        else -> actionLauncher.launch(permissions)
    }
}