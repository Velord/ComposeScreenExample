package com.velord.ui.feature.demo.dialog.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
internal actual fun platformDialogProperties(): DialogProperties = DialogProperties(
    scrimColor = Color.Transparent,
    animateTransition = false,
)

@Composable
internal actual fun ConfigureDialogWindow() = Unit
