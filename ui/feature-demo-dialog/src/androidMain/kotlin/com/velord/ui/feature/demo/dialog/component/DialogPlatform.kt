package com.velord.ui.feature.demo.dialog.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider

@Composable
internal actual fun ConfigureDialogWindow() {
    val window = (LocalView.current.parent as? DialogWindowProvider)?.window

    SideEffect {
        window?.apply {
            setDimAmount(0f)
            setWindowAnimations(-1)
        }
    }
}
