package com.velord.navigation.compose.destinations.graph

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.velord.bottomnavigation.screen.compose.BottomNavigationScreen
import com.velord.bottomnavigation.screen.compose.BottomNavigator
import com.velord.navigation.compose.destinations.transition.PopScaleOutTransition

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