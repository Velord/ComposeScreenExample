package com.velord.infrastructure.util.persmission

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.velord.infrastructure.util.permission.PermissionGrantState

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionStatus.toPermissionGrantState(
    isRequestedBefore: Boolean
): PermissionGrantState = when (this) {
    is PermissionStatus.Granted -> PermissionGrantState.Granted
    is PermissionStatus.Denied -> when {
        this.shouldShowRationale -> PermissionGrantState.Rationale
        isRequestedBefore -> PermissionGrantState.PossiblePermanentDeny
        else -> PermissionGrantState.Denied
    }
}