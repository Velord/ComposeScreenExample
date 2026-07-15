package com.velord.ui.feature.camerarecording

import androidx.compose.runtime.Composable
import com.velord.infrastructure.util.permission.PermissionGrantState
import kotlinx.coroutines.flow.MutableSharedFlow
import com.velord.core.ui.permission.CheckCameraAndAudioRecordPermission as CheckAndroidPermission

@Composable
internal actual fun CheckCameraAndAudioRecordPermission(
    triggerCheckEvent: MutableSharedFlow<Unit>,
    onCameraUpdateState: (PermissionGrantState) -> Unit,
    onMicroUpdateState: (PermissionGrantState) -> Unit,
) {
    CheckAndroidPermission(
        triggerCheckEvent = triggerCheckEvent,
        onCameraUpdateState = onCameraUpdateState,
        onMicroUpdateState = onMicroUpdateState,
    )
}
