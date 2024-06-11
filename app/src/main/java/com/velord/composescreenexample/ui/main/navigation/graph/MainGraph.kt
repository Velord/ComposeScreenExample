package com.velord.composescreenexample.ui.main.navigation.graph

import com.ramcosta.composedestinations.annotation.ExternalDestination
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.ramcosta.composedestinations.generated.modulebottomnavigation.destinations.BottomNavigationDestinationDestination
import com.velord.composescreenexample.ui.main.navigation.DefaultTransitions

private const val MAIN_GRAPH = "main_nav_graph"
@NavHostGraph(
    defaultTransitions = DefaultTransitions::class,
    route = MAIN_GRAPH,
    visibility = CodeGenVisibility.INTERNAL
)
annotation class MainGraph {
    @ExternalDestination<BottomNavigationDestinationDestination>(start = true)
    companion object Includes
}