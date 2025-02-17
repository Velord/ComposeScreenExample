package com.velord.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
import com.velord.navigation.compose.vanilla.DemoDestinationVanilla
import com.velord.navigation.compose.vanilla.FlowSummatorDestinationVanilla
import com.velord.navigation.compose.vanilla.HintPhoneDestinationVanilla
import com.velord.navigation.compose.vanilla.ModifierDestinationVanilla
import com.velord.navigation.compose.vanilla.MorphDemoDestinationVanilla
import com.velord.navigation.compose.vanilla.MovieDestinationVanilla
import com.velord.navigation.compose.vanilla.SettingsDestinationVanilla
import com.velord.navigation.compose.vanilla.ShapeDemoDestinationVanilla
import com.velord.navigation.compose.vanilla.navigator.BottomNavigatorVanilla
import com.velord.settings.SettingsScreen
import com.velord.shapedemo.ShapeDemoScreen
import com.velord.sharedviewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

internal fun NavGraphBuilder.setupBottomNavigationGraph(navigator: BottomNavigatorVanilla) {
    composable<DemoDestinationVanilla> {
        val viewModel = koinViewModel<DemoViewModel>()

        DemoScreen(viewModel) {
            navigator.goTo(it)
        }
    }

    composable<SettingsDestinationVanilla> {
        val viewModel = koinViewModel<ThemeViewModel>()
        SettingsScreen(viewModel)
    }

    setupCameraRecordingGraph(navigator)

    composable<ShapeDemoDestinationVanilla> {
        ShapeDemoScreen()
    }

    composable<ModifierDestinationVanilla> {
        ModifierDemoScreen()
    }

    composable<FlowSummatorDestinationVanilla> {
        val viewModel = koinViewModel<FlowSummatorViewModel>()
        FlowSummatorScreen(viewModel)
    }

    composable<MorphDemoDestinationVanilla> {
        MorphDemoScreen()
    }

    composable<HintPhoneDestinationVanilla> {
        HintPhoneNumberScreen()
    }

    composable<MovieDestinationVanilla> {
        val viewModel = koinViewModel<MovieViewModel>()
        val allMovieViewModel = koinViewModel<AllMovieViewModel>()
        val favoriteMovieViewModel = koinViewModel<FavoriteMovieViewModel>()
        MovieScreen(viewModel, allMovieViewModel, favoriteMovieViewModel)
    }
}