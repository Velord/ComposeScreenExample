package com.velord.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.dialogDemo.DialogDemoScreen
import com.velord.dialogDemo.DialogDemoViewModel
import org.koin.androidx.compose.koinViewModel

internal object DialogDemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<DialogDemoViewModel>()
        DialogDemoScreen(viewModel)
    }
}