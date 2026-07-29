package com.velord.infrastructure.navigation.compose.destinations.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationUiAction
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVM
import com.velord.ui.feature.demo.DemoNavigator
import com.velord.ui.feature.demo.DemoScreen
import com.velord.ui.feature.demo.DemoVM
import com.velord.ui.feature.demo.dialog.DialogDemoScreen
import com.velord.ui.feature.demo.dialog.DialogDemoVM
import com.velord.ui.feature.demo.hintphonenumber.HintPhoneNumberScreen
import com.velord.ui.feature.demo.modifier.ModifierDemoScreen
import com.velord.ui.feature.demo.morph.MorphDemoScreen
import com.velord.ui.feature.demo.shape.ShapeDemoScreen
import com.velord.ui.feature.flowsummator.FlowSummatorScreen
import com.velord.ui.feature.flowsummator.FlowSummatorVM
import com.velord.ui.feature.movie.MovieScreen
import com.velord.ui.feature.movie.viewModel.AllMovieVM
import com.velord.ui.feature.movie.viewModel.FavoriteMovieVM
import com.velord.ui.feature.movie.viewModel.MovieVM
import org.koin.compose.viewmodel.koinViewModel

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
    val viewModel = koinViewModel<DemoVM>()
    val bottomNavVM = koinViewModel<BottomNavigationVM>()
    val context = LocalContext.current

    DemoScreen(
        viewModel = viewModel,
        onGraphCompleted = {
            bottomNavVM.onAction(BottomNavigationUiAction.GraphTakeResponsibility)
        },
        onNavigationEvent = {
            navigator.goTo(it)
        },
        onBackClick = {
            bottomNavVM.onAction(BottomNavigationUiAction.BackRequest)
        },
    )
}

@Destination<DemoGraph>
@Composable
internal fun ShapeDemoDestination(navigator: DemoNavigator) {
    ShapeDemoScreen(onBackClick = navigator::goBack)
}

@Destination<DemoGraph>
@Composable
internal fun ModifierDemoDestination(navigator: DemoNavigator) {
    ModifierDemoScreen(onBackClick = navigator::goBack)
}

@Destination<DemoGraph>
@Composable
internal fun FlowSummatorDestination(navigator: DemoNavigator) {
    val viewModel = koinViewModel<FlowSummatorVM>()
    FlowSummatorScreen(
        viewModel = viewModel,
        onBackClick = navigator::goBack
    )
}

@Destination<DemoGraph>
@Composable
internal fun MorphDemoDestination(navigator: DemoNavigator) {
    MorphDemoScreen(onBackClick = navigator::goBack)
}

@Destination<DemoGraph>
@Composable
internal fun HintPhoneNumberDestination(navigator: DemoNavigator) {
    HintPhoneNumberScreen(onBackClick = navigator::goBack)
}

@Destination<DemoGraph>
@Composable
internal fun MovieDestination(navigator: DemoNavigator) {
    val viewModel = koinViewModel<MovieVM>()
    val allMovieVM = koinViewModel<AllMovieVM>()
    val favoriteMovieVM = koinViewModel<FavoriteMovieVM>()
    MovieScreen(
        viewModel = viewModel,
        allMovieVM = allMovieVM,
        favoriteMovieVM = favoriteMovieVM,
        onBackClick = navigator::goBack
    )
}

@Destination<DemoGraph>
@Composable
internal fun DialogDestination(navigator: DemoNavigator) {
    val viewModel = koinViewModel<DialogDemoVM>()
    DialogDemoScreen(
        viewModel = viewModel,
        onBackClick = navigator::goBack
    )
}
