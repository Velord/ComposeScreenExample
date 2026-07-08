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
import com.velord.ui.feature.demo.DemoDestinationNavigationEvent
import com.velord.ui.feature.demo.DemoScreen
import com.velord.ui.feature.demo.DemoViewModel
import org.koin.androidx.compose.koinViewModel

internal object DemoVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<DemoViewModel>()
        val bottomNavViewModel = koinViewModel<BottomNavigationDestinationsVM>()
        val navigator = LocalNavigator.currentOrThrow
        val navigationEvent = viewModel.navigationEventDestination.collectAsStateWithLifecycle(
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

private fun DemoDestinationNavigationEvent.voyagerScreenProvider(): SharedScreenVoyager.Demo =
    when (this) {
        DemoDestinationNavigationEvent.Shape -> SharedScreenVoyager.Demo.Shape
        DemoDestinationNavigationEvent.Modifier -> SharedScreenVoyager.Demo.Modifier
        DemoDestinationNavigationEvent.FlowSummator -> SharedScreenVoyager.Demo.FlowSummator
        DemoDestinationNavigationEvent.Morph -> SharedScreenVoyager.Demo.Morph
        DemoDestinationNavigationEvent.HintPhoneNumber -> SharedScreenVoyager.Demo.HintPhoneNumber
        DemoDestinationNavigationEvent.Movie -> SharedScreenVoyager.Demo.Movie
        DemoDestinationNavigationEvent.Dialog -> SharedScreenVoyager.Demo.Dialog
}
