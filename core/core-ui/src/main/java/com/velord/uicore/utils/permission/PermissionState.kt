package com.velord.uicore.utils.permission

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionStatus.toAndroidPermissionState(isRequestedBefore: Boolean): com.velord.util.permission.AndroidPermissionState = when (this) {
    is PermissionStatus.Granted -> com.velord.util.permission.AndroidPermissionState.Granted
    is PermissionStatus.Denied -> when {
        this.shouldShowRationale -> com.velord.util.permission.AndroidPermissionState.Rationale
        this.shouldShowRationale.not() && isRequestedBefore -> com.velord.util.permission.AndroidPermissionState.PossiblePermanentDeny
        else -> com.velord.util.permission.AndroidPermissionState.Denied
    }
}