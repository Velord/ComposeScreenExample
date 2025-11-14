package com.velord.core.ui.utils.permission

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.velord.util.permission.AndroidPermissionState

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionStatus.toAndroidPermissionState(isRequestedBefore: Boolean): AndroidPermissionState = when (this) {
    is PermissionStatus.Granted -> AndroidPermissionState.Granted
    is PermissionStatus.Denied -> when {
        this.shouldShowRationale -> AndroidPermissionState.Rationale
        this.shouldShowRationale.not() && isRequestedBefore -> AndroidPermissionState.PossiblePermanentDeny
        else -> AndroidPermissionState.Denied
    }
}