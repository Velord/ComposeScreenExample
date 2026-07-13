package com.velord.infrastructure.navigation.compose.destinations.graph

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.velord.infrastructure.navigation.compose.destinations.transition.PopFadeTransition
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.ui.feature.setting.SettingScreen
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.androidx.compose.koinViewModel

private const val BOTTOM_NAVIGATION_GRAPH = "bottom_navigation_graph"
@NavHostGraph(
    defaultTransitions = PopFadeTransition::class,
    route = BOTTOM_NAVIGATION_GRAPH,
    visibility = CodeGenVisibility.INTERNAL
)
annotation class BottomNavigationGraph

@Destination<BottomNavigationGraph>
@Destination<MainGraph>
@Composable
internal fun SettingsDestination() {
    val viewModel = koinViewModel<ThemeVM>()
    val bottomNavVM = koinViewModel<BottomNavigationDestinationsVM>()
    SettingScreen(
        viewModel = viewModel,
        onBackClick = {
            bottomNavVM.graphCompletedHandling()
        }
    )
}
