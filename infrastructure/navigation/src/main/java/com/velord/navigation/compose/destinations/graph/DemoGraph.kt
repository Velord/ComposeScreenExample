package com.velord.navigation.compose.destinations.graph

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.velord.dialogDemo.DialogDemoScreen
import com.velord.dialogDemo.DialogDemoViewModel
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
import com.velord.shapedemo.ShapeDemoScreen
import org.koin.androidx.compose.koinViewModel

private const val DEMO_GRAPH = "demo_graph"
@NavGraph<BottomNavigationGraph>(
    route = DEMO_GRAPH,
    visibility = CodeGenVisibility.INTERNAL,
    start = true
)
annotation class DemoGraph

@Destination<DemoGraph>(start = true)
@Composable
internal fun DemoDestination(navigator: DemoNavigator) {
    val viewModel = koinViewModel<DemoViewModel>()
    DemoScreen(viewModel) {
        navigator.goTo(it)
    }
}

@Destination<DemoGraph>
@Composable
internal fun ShapeDemoDestination() {
    ShapeDemoScreen()
}

@Destination<DemoGraph>
@Composable
internal fun ModifierDemoDestination() {
    ModifierDemoScreen()
}

@Destination<DemoGraph>
@Composable
internal fun FlowSummatorDestination() {
    val viewModel = koinViewModel<FlowSummatorViewModel>()
    FlowSummatorScreen(viewModel)
}

@Destination<DemoGraph>
@Composable
internal fun MorphDemoDestination() {
    MorphDemoScreen()
}

@Destination<DemoGraph>
@Composable
internal fun HintPhoneNumberDestination() {
    HintPhoneNumberScreen()
}

@Destination<DemoGraph>
@Composable
internal fun MovieDestination() {
    val viewModel = koinViewModel<MovieViewModel>()
    val allMovieViewModel = koinViewModel<AllMovieViewModel>()
    val favoriteMovieViewModel = koinViewModel<FavoriteMovieViewModel>()
    MovieScreen(viewModel, allMovieViewModel, favoriteMovieViewModel)
}

@Destination<DemoGraph>
@Composable
internal fun DialogDestination() {
    val viewModel = koinViewModel<DialogDemoViewModel>()
    DialogDemoScreen(viewModel)
}