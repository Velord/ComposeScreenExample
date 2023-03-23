package com.velord.composescreenexample.utils.context

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import java.io.File

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun Context.checkCameraHardware(): Boolean =
    packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

fun Context.createSettingsIntent() = Intent().apply {
    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    data = Uri.fromParts("package", packageName, null)
}

fun Context.createDirInCache(dirName: String) = externalCacheDirs.firstOrNull()?.let {
    File(it, dirName).apply { mkdirs() }
}

fun Context.createDirInCacheOrFilesDir(dirName: String): File {
    val mediaDir = createDirInCache(dirName)
    return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
}