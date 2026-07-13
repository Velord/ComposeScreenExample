package com.velord.infrastructure.navigation.compose.destinations.graph

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.velord.infrastructure.navigation.compose.destinations.transition.PopScaleOutTransition
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigator
import com.velord.ui.feature.bottomnavigation.screen.compose.BottomNavigationScreen

private const val MAIN_GRAPH = "main_nav_graph"
@NavHostGraph(
    defaultTransitions = PopScaleOutTransition::class,
    route = MAIN_GRAPH,
    visibility = CodeGenVisibility.PUBLIC
)
annotation class MainGraph

@Destination<MainGraph>(start = true)
@Composable
internal fun BottomNavigationDestination(navigator: BottomNavigator) {
    BottomNavigationScreen(navigator)
}
