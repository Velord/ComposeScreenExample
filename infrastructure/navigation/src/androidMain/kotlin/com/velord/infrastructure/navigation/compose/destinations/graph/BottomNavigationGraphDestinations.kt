package com.velord.infrastructure.navigation.compose.destinations.graph

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.velord.infrastructure.navigation.compose.destinations.navigator.SupremeNavigatorDestinations
import com.velord.infrastructure.navigation.compose.destinations.transition.PopFadeTransition
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVM
import com.velord.ui.feature.setting.SettingScreen
import com.velord.ui.sharedviewmodel.LanguageVM
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.compose.viewmodel.koinViewModel

private const val BOTTOM_NAVIGATION_GRAPH = "bottom_navigation_graph"

@NavHostGraph(
    defaultTransitions = PopFadeTransition::class,
    route = BOTTOM_NAVIGATION_GRAPH,
    visibility = CodeGenVisibility.INTERNAL,
)
annotation class BottomNavigationGraph

@Destination<BottomNavigationGraph>
@Composable
internal fun BottomNavigationSettingsDestination() {
    val themeViewModel = koinViewModel<ThemeVM>()
    val languageViewModel = koinViewModel<LanguageVM>()
    val bottomNavVM = koinViewModel<BottomNavigationVM>()
    SettingScreen(
        themeViewModel = themeViewModel,
        languageViewModel = languageViewModel,
        onGraphCompleted = {
            bottomNavVM.onAction(BottomNavigationUiAction.GraphCompletedHandling)
        },
        onBackClick = {
            bottomNavVM.onAction(BottomNavigationUiAction.BackRequest)
        },
    )
}

@Destination<MainGraph>
@Composable
internal fun MainSettingsDestination(navigator: SupremeNavigatorDestinations) {
    val themeViewModel = koinViewModel<ThemeVM>()
    val languageViewModel = koinViewModel<LanguageVM>()
    SettingScreen(
        themeViewModel = themeViewModel,
        languageViewModel = languageViewModel,
        onGraphCompleted = {},
        onBackClick = navigator::goBack,
    )
}
