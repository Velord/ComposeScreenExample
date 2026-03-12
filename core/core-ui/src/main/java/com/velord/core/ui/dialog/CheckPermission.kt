package com.velord.core.ui.dialog

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
<<<<<<<< HEAD:core-ui/src/main/java/com/velord/uicore/dialog/CheckPermission.kt
import com.velord.resource.R
========
import com.velord.core.resource.R
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:core/core-ui/src/main/java/com/velord/core/ui/dialog/CheckPermission.kt
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

<<<<<<<< HEAD:core-ui/src/main/java/com/velord/uicore/dialog/CheckPermission.kt
fun Context.showRationalePermissionForMic(
========
fun Context.showGoToSettingsForMic(
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:core/core-ui/src/main/java/com/velord/core/ui/dialog/CheckPermission.kt
    onDecline: () -> Unit
) {
    askUserActivatePermissionInSettings(
        title = R.string.require_microphone_permission,
        message = R.string.give_access_to_microphone,
        onDecline = onDecline
    )
}

<<<<<<<< HEAD:core-ui/src/main/java/com/velord/uicore/dialog/CheckPermission.kt
fun Context.showRationalePermissionForCamera(
========
fun Context.showGoToSettingsForCamera(
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:core/core-ui/src/main/java/com/velord/core/ui/dialog/CheckPermission.kt
    onDecline: () -> Unit
) {
    askUserActivatePermissionInSettings(
        title = R.string.require_camera_permission,
        message = R.string.give_access_to_camera,
        onDecline = onDecline
    )
}

<<<<<<<< HEAD:core-ui/src/main/java/com/velord/uicore/dialog/CheckPermission.kt
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
            requireContext().showRationalePermissionForMic {}
        else -> actionLauncher.launch(permRecordAudio)
    }
}

========
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:core/core-ui/src/main/java/com/velord/core/ui/dialog/CheckPermission.kt
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
<<<<<<<< HEAD:core-ui/src/main/java/com/velord/uicore/dialog/CheckPermission.kt
            requireContext().showRationalePermissionForMic(onDecline)
        shouldShowRequestPermissionRationale(permCamera) ->
            requireContext().showRationalePermissionForCamera(onDecline)
========
            requireContext().showGoToSettingsForMic(onDecline)
        shouldShowRequestPermissionRationale(permCamera) ->
            requireContext().showGoToSettingsForCamera(onDecline)
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:core/core-ui/src/main/java/com/velord/core/ui/dialog/CheckPermission.kt
        else -> actionLauncher.launch(permissions)
    }
}