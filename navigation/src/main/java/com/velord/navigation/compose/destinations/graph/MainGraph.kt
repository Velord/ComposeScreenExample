package com.velord.navigation.compose.destinations.graph

import com.ramcosta.composedestinations.annotation.ExternalDestination
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.ramcosta.composedestinations.generated.modulebottomnavigation.destinations.BottomNavigationDestinationDestination
import com.velord.navigation.compose.destinations.DefaultTransitions

private const val MAIN_GRAPH = "main_nav_graph"
@NavHostGraph(
    defaultTransitions = DefaultTransitions::class,
    route = MAIN_GRAPH,
    visibility = CodeGenVisibility.PUBLIC
)
annotation class MainGraph {
    @ExternalDestination<BottomNavigationDestinationDestination>(start = true)
    companion object Includes
}