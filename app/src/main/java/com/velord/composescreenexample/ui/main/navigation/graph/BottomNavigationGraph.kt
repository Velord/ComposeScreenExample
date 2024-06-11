package com.velord.composescreenexample.ui.main.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.velord.feature.demo.DemoNavigator
import com.velord.feature.demo.DemoScreen
import com.velord.feature.demo.DemoViewModel
import com.velord.flowsummator.FlowSummatorScreen
import com.velord.flowsummator.FlowSummatorViewModel
import com.velord.hintphonenumber.HintPhoneNumberScreen
import com.velord.modifierdemo.ModifierDemoScreen
import com.velord.morphdemo.MorphDemoScreen
import com.velord.settings.SettingsScreen
import com.velord.shapedemo.ShapeDemoScreen
import com.velord.sharedviewmodel.ThemeViewModel
import kotlinx.coroutines.flow.filterNotNull
import org.koin.androidx.compose.koinViewModel

private const val BOTTOM_NAVIGATION_GRAPH = "bottom_navigation_graph"
@NavHostGraph(route = BOTTOM_NAVIGATION_GRAPH)
annotation class BottomNavigationGraph

@Destination<BottomNavigationGraph>
@Destination<CameraRecordingGraph>
@Composable
fun SettingsDestination() {
    val viewModel = koinViewModel<ThemeViewModel>()
    SettingsScreen(viewModel)
}

@Destination<BottomNavigationGraph>
@Composable
fun DemoDestination(navigator: DemoNavigator) {
    val viewModel = koinViewModel<DemoViewModel>()
    val destinationEventState = viewModel.navigationEventDestination
        .collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(key1 = destinationEventState) {
        snapshotFlow { destinationEventState.value }
            .filterNotNull()
            .collect {
                navigator.goTo(it)
            }
    }

    DemoScreen(viewModel)
}

@Destination<BottomNavigationGraph>
@Composable
fun ShapeDemoDestination() {
    ShapeDemoScreen()
}

@Destination<BottomNavigationGraph>
@Composable
fun ModifierDemoDestination() {
    ModifierDemoScreen()
}

@Destination<BottomNavigationGraph>
@Composable
fun FlowSummatorDestination() {
    val viewmodel = koinViewModel<FlowSummatorViewModel>()
    FlowSummatorScreen(viewmodel)
}

@Destination<BottomNavigationGraph>
@Composable
fun MorphDemoDestination() {
    MorphDemoScreen()
}

@Destination<BottomNavigationGraph>
@Composable
fun HintPhoneNumberDestination() {
    HintPhoneNumberScreen()
}