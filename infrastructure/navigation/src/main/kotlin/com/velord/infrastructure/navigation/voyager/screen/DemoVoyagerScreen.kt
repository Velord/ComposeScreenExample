package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.velord.core.navigation.voyager.SharedScreenVoyager
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.ui.feature.demo.DemoNavigationEvent
import com.velord.ui.feature.demo.DemoScreen
import com.velord.ui.feature.demo.DemoVM
import org.koin.androidx.compose.koinViewModel

internal object DemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<DemoVM>()
        val bottomNavVM = koinViewModel<BottomNavigationDestinationsVM>()
        val navigator = LocalNavigator.currentOrThrow
        val navigationEvent = viewModel.navigationEvent.collectAsStateWithLifecycle(
            initialValue = null,
        )
        val screen = navigationEvent.value?.let {
            rememberScreen(it.voyagerScreenProvider())
        }
        LaunchedEffect(key1 = screen) {
            screen?.let {
                navigator.push(it)
            }
        }

        DemoScreen(
            viewModel = viewModel,
            onNavigationEvent = {}, // Handled by LaunchedEffect
            onBackClick = {
                // To enable System Back Button handling
                // via Bottom Navigation -> comment the line below
                // bottomNavViewModel.graphCompletedHandling()
            },
        )
    }
}

private fun DemoNavigationEvent.voyagerScreenProvider(): SharedScreenVoyager.Demo = when (this) {
        DemoNavigationEvent.Shape -> SharedScreenVoyager.Demo.Shape
        DemoNavigationEvent.Modifier -> SharedScreenVoyager.Demo.Modifier
        DemoNavigationEvent.FlowSummator -> SharedScreenVoyager.Demo.FlowSummator
        DemoNavigationEvent.Morph -> SharedScreenVoyager.Demo.Morph
        DemoNavigationEvent.HintPhoneNumber -> SharedScreenVoyager.Demo.HintPhoneNumber
        DemoNavigationEvent.Movie -> SharedScreenVoyager.Demo.Movie
        DemoNavigationEvent.Dialog -> SharedScreenVoyager.Demo.Dialog
}
