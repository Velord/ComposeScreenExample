package com.velord.uicore.utils.permission

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionStatus.toPermissionState(): com.velord.util.permission.AndroidPermissionState = when (this) {
    is PermissionStatus.Granted -> com.velord.util.permission.AndroidPermissionState.Granted
    is PermissionStatus.Denied -> if (this.shouldShowRationale) {
        com.velord.util.permission.AndroidPermissionState.Rationale
    } else {
        com.velord.util.permission.AndroidPermissionState.Denied
    }
}