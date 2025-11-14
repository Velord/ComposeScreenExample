package com.velord.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
import com.velord.navigation.compose.vanilla.GraphVanilla
import com.velord.shapedemo.ShapeDemoScreen
import org.koin.androidx.compose.koinViewModel

internal fun NavGraphBuilder.setupDemoGraph(navigator: DemoNavigator) {
    navigation<GraphVanilla.BottomTab.Demo.Self>(startDestination = GraphVanilla.BottomTab.Demo.DemoDestinationVanilla) {
        composable<GraphVanilla.BottomTab.Demo.DemoDestinationVanilla> {
            val viewModel = koinViewModel<DemoViewModel>()
            DemoScreen(viewModel) {
                navigator.goTo(it)
            }
        }

        composable<GraphVanilla.BottomTab.Demo.ShapeDemoDestinationVanilla> {
            ShapeDemoScreen()
        }

        composable<GraphVanilla.BottomTab.Demo.ModifierDestinationVanilla> {
            ModifierDemoScreen()
        }

        composable<GraphVanilla.BottomTab.Demo.FlowSummatorDestinationVanilla> {
            val viewModel = koinViewModel<FlowSummatorViewModel>()
            FlowSummatorScreen(viewModel)
        }

        composable<GraphVanilla.BottomTab.Demo.MorphDemoDestinationVanilla> {
            MorphDemoScreen()
        }

        composable<GraphVanilla.BottomTab.Demo.HintPhoneDestinationVanilla> {
            HintPhoneNumberScreen()
        }

        composable<GraphVanilla.BottomTab.Demo.MovieDestinationVanilla> {
            val viewModel = koinViewModel<MovieViewModel>()
            val allMovieViewModel = koinViewModel<AllMovieViewModel>()
            val favoriteMovieViewModel = koinViewModel<FavoriteMovieViewModel>()
            MovieScreen(viewModel, allMovieViewModel, favoriteMovieViewModel)
        }

        composable<GraphVanilla.BottomTab.Demo.DialogDestinationVanilla> {
            val viewModel = koinViewModel<DialogDemoViewModel>()
            DialogDemoScreen(viewModel)
        }
    }
}