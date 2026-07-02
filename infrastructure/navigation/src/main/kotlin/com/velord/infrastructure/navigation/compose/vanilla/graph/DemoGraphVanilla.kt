package com.velord.infrastructure.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
import com.velord.infrastructure.navigation.compose.vanilla.GraphVanilla
import com.velord.ui.feature.demo.modifier.ModifierDemoScreen
import com.velord.ui.feature.demo.morph.MorphDemoScreen
import com.velord.ui.feature.demo.shape.ShapeDemoScreen
import org.koin.androidx.compose.koinViewModel

internal fun NavGraphBuilder.setupDemoGraphVanilla(navigator: DemoNavigator) {
    navigation<GraphVanilla.BottomTab.Demo.Self>(
        startDestination = GraphVanilla.BottomTab.Demo.DemoDestinationVanilla
    ) {
        composable<GraphVanilla.BottomTab.Demo.DemoDestinationVanilla> {
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
