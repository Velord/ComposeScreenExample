package com.velord.core.ui.dialog

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.velord.core.resource.Res
import com.velord.core.resource.decline
import com.velord.core.resource.give_access_to_camera
import com.velord.core.resource.give_access_to_microphone
import com.velord.core.resource.go_to_app_settings
import com.velord.core.resource.require_camera_permission
import com.velord.core.resource.require_microphone_permission
import com.velord.util.context.createSettingsIntent
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString

private fun Context.askUserActivatePermissionInSettings(
    title: org.jetbrains.compose.resources.StringResource,
    message: org.jetbrains.compose.resources.StringResource,
    onDecline: () -> Unit
) {
    alertDialog(
        title = runBlocking { getString(title) },
        message = runBlocking { getString(message) },
        positiveText = runBlocking { getString(Res.string.go_to_app_settings) },
        negativeText = runBlocking { getString(Res.string.decline) },
        positiveCallback = {
            startActivity(createSettingsIntent())
        },
        negativeCallback = onDecline,
        cancelable = false
    )
}

fun Context.showGoToSettingsForMic(
    onDecline: () -> Unit
) {
    askUserActivatePermissionInSettings(
        title = Res.string.require_microphone_permission,
        message = Res.string.give_access_to_microphone,
        onDecline = onDecline
    )
}

fun Context.showGoToSettingsForCamera(
    onDecline: () -> Unit
) {
    askUserActivatePermissionInSettings(
        title = Res.string.require_camera_permission,
        message = Res.string.give_access_to_camera,
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
        shouldShowRequestPermissionRationale(permRecordAudio) ->
            requireContext().showGoToSettingsForMic {}
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

    val isGranted = permissions.all { permission ->
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    when {
        isGranted -> onGranted()
        shouldShowRequestPermissionRationale(permRecordAudio) ->
            requireContext().showGoToSettingsForMic(onDecline)
        shouldShowRequestPermissionRationale(permCamera) ->
            requireContext().showGoToSettingsForCamera(onDecline)
        else -> actionLauncher.launch(permissions)
    }
}
