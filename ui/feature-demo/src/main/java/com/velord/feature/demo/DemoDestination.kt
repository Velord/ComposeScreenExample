package com.velord.feature.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import kotlinx.coroutines.flow.filterNotNull
import org.koin.androidx.compose.koinViewModel

enum class DemoDest {
    Shape,
    Modifier,
    FlowSummator,
    Morph
}

interface DemoNavigator {
    fun goTo(dest: DemoDest)
}

@Destination<ExternalModuleGraph>
@Composable
fun DemoDestination(
    navigator: DemoNavigator
) {
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