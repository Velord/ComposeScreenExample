package com.velord.bottomnavigation

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.annotation.NavGraph
import com.velord.bottomnavigation.screen.BottomNavigationVoyagerScreen
import com.velord.bottomnavigation.viewmodel.BottomNavViewModelVoyager
import org.koin.androidx.compose.koinViewModel

private const val BOTTOM_NAVIGATION_GRAPH = "bottom_navigation_graph"
@NavGraph<ExternalModuleGraph>(
    route = BOTTOM_NAVIGATION_GRAPH,
    start = true,
    navArgs = Nothing::class,
    deepLinks = []
)
internal annotation class BottomNavigationGraph

@Destination<BottomNavigationGraph>(start = true)
@Composable
internal fun BottomNavigationDestination() {
    val viewModel = koinViewModel<BottomNavViewModelVoyager>()
    BottomNavigationVoyagerScreen(viewModel)
}