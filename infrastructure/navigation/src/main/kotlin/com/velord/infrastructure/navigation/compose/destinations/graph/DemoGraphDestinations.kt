package com.velord.infrastructure.navigation.compose.destinations.graph

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
import com.velord.ui.feature.demo.dialog.DialogDemoScreen
import com.velord.ui.feature.demo.dialog.DialogDemoViewModel
import com.velord.ui.feature.demo.DemoNavigator
import com.velord.ui.feature.demo.DemoScreen
import com.velord.ui.feature.demo.DemoViewModel
import com.velord.ui.feature.movie.MovieScreen
import com.velord.ui.feature.movie.viewModel.AllMovieViewModel
import com.velord.ui.feature.movie.viewModel.FavoriteMovieViewModel
import com.velord.ui.feature.movie.viewModel.MovieViewModel
import com.velord.ui.feature.flowsummator.FlowSummatorScreen
import com.velord.ui.feature.flowsummator.FlowSummatorViewModel
import com.velord.ui.feature.demo.hintphonenumber.HintPhoneNumberScreen
import com.velord.ui.feature.demo.modifier.ModifierDemoScreen
import com.velord.ui.feature.demo.morph.MorphDemoScreen
import com.velord.ui.feature.demo.shape.ShapeDemoScreen
import org.koin.androidx.compose.koinViewModel

private const val DEMO_GRAPH = "demo_graph"
@NavGraph<BottomNavigationGraph>(
    start = true,
    route = DEMO_GRAPH,
    visibility = CodeGenVisibility.INTERNAL,
)
annotation class DemoGraph

@Destination<DemoGraph>(start = true)
@Composable
internal fun DemoDestination(navigator: DemoNavigator) {
    val viewModel = koinViewModel<DemoViewModel>()
    val bottomNavViewModel = koinViewModel<BottomNavigationDestinationsVM>()

    DemoScreen(
        viewModel = viewModel,
        onNavigationEvent = {
            navigator.goTo(it)
        },
        onBackClick = {
            // To enable System Back Button handling
            // via Bottom Navigation -> comment the line below
            // bottomNavViewModel.graphCompletedHandling()
        }
    )
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
