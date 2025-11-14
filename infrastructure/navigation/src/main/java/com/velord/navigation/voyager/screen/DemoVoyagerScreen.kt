package com.velord.navigation.voyager.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.velord.feature.demo.DemoScreen
import com.velord.feature.demo.DemoViewModel
import org.koin.androidx.compose.koinViewModel

internal object DemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<DemoViewModel>()
        val navigationEvent = viewModel.navigationEventVoyager.collectAsStateWithLifecycle(initialValue = null)

        val navigator = LocalNavigator.currentOrThrow
        val screen: Screen? = navigationEvent.value?.let {
            rememberScreen(it.screen)
        }
        LaunchedEffect(key1 = screen) {
            screen?.let {
                navigator.push(it)
            }
        }

        DemoScreen(viewModel) {}
    }
}