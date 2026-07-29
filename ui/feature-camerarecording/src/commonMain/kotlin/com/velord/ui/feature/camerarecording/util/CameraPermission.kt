package com.velord.ui.feature.camerarecording.util

import androidx.compose.runtime.Composable
import com.velord.infrastructure.util.permission.PermissionGrantState
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
internal expect fun CheckCameraAndAudioRecordPermission(
    triggerCheckEvent: MutableSharedFlow<Unit>,
    onCameraUpdateState: (PermissionGrantState) -> Unit,
    onMicroUpdateState: (PermissionGrantState) -> Unit,
)
