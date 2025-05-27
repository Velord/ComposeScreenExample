package com.velord.navigation.compose.destinations.graph

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.velord.feature.demo.DemoNavigator
import com.velord.feature.demo.DemoScreen
import com.velord.feature.demo.DemoViewModel
import com.velord.feature.movie.MovieScreen
import com.velord.feature.movie.viewModel.AllMovieViewModel
import com.velord.feature.movie.viewModel.FavoriteMovieViewModel
import com.velord.feature.movie.viewModel.MovieViewModel
import com.velord.flowsummator.FlowSummatorScreen
import com.velord.flowsummator.FlowSummatorViewModel
import com.velord.hintphonenumber.HintPhoneNumberScreen
import com.velord.modifierdemo.ModifierDemoScreen
import com.velord.morphdemo.MorphDemoScreen
import com.velord.navigation.compose.destinations.transition.PopFadeTransition
import com.velord.settings.SettingsScreen
import com.velord.shapedemo.ShapeDemoScreen
import com.velord.sharedviewmodel.ThemeViewModel
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
    val viewModel = koinViewModel<ThemeViewModel>()
    SettingsScreen(viewModel)
}

@Destination<BottomNavigationGraph>(start = true)
@Composable
internal fun DemoDestination(navigator: DemoNavigator) {
    val viewModel = koinViewModel<DemoViewModel>()
    DemoScreen(viewModel) {
        navigator.goTo(it)
    }
}

@Destination<BottomNavigationGraph>
@Composable
internal fun ShapeDemoDestination() {
    ShapeDemoScreen()
}

@Destination<BottomNavigationGraph>
@Composable
internal fun ModifierDemoDestination() {
    ModifierDemoScreen()
}

@Destination<BottomNavigationGraph>
@Composable
internal fun FlowSummatorDestination() {
    val viewModel = koinViewModel<FlowSummatorViewModel>()
    FlowSummatorScreen(viewModel)
}

@Destination<BottomNavigationGraph>
@Composable
internal fun MorphDemoDestination() {
    MorphDemoScreen()
}

@Destination<BottomNavigationGraph>
@Composable
internal fun HintPhoneNumberDestination() {
    HintPhoneNumberScreen()
}

@Destination<BottomNavigationGraph>
@Composable
internal fun MovieDestination() {
    val viewModel = koinViewModel<MovieViewModel>()
    val allMovieViewModel = koinViewModel<AllMovieViewModel>()
    val favoriteMovieViewModel = koinViewModel<FavoriteMovieViewModel>()
    MovieScreen(viewModel, allMovieViewModel, favoriteMovieViewModel)
}