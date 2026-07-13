package com.velord.ui.feature.demo.dialog.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

internal expect fun platformDialogProperties(): DialogProperties

@Composable
internal expect fun ConfigureDialogWindow()
