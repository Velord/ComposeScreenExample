package com.velord.ui.feature.camerarecording.viewModel

import com.velord.infrastructure.util.permission.PermissionGrantState

internal actual fun defaultPermissionUiState(): PermissionUiState = PermissionUiState(
    camera = PermissionGrantState.NotAsked,
    audio = PermissionGrantState.NotAsked,
)
