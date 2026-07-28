package com.velord.infrastructure.navigation.compose.vanilla.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.velord.infrastructure.navigation.compose.vanilla.GraphVanilla
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

internal fun NavGraphBuilder.setupDemoGraphVanilla(navigator: DemoNavigator) {
    navigation<GraphVanilla.BottomTab.Demo.Self>(
        startDestination = GraphVanilla.BottomTab.Demo.DemoDestinationVanilla
    ) {
        composable<GraphVanilla.BottomTab.Demo.DemoDestinationVanilla> {
            val viewModel = koinViewModel<DemoVM>()
            val bottomNavVM = koinViewModel<BottomNavigationVM>()

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

        composable<GraphVanilla.BottomTab.Demo.ShapeDemoDestinationVanilla> {
            ShapeDemoScreen(onBackClick = navigator::goBack)
        }

        composable<GraphVanilla.BottomTab.Demo.ModifierDestinationVanilla> {
            ModifierDemoScreen(onBackClick = navigator::goBack)
        }

        composable<GraphVanilla.BottomTab.Demo.FlowSummatorDestinationVanilla> {
            val viewModel = koinViewModel<FlowSummatorVM>()
            FlowSummatorScreen(
                viewModel = viewModel,
                onBackClick = navigator::goBack
            )
        }

        composable<GraphVanilla.BottomTab.Demo.MorphDemoDestinationVanilla> {
            MorphDemoScreen(onBackClick = navigator::goBack)
        }

        composable<GraphVanilla.BottomTab.Demo.HintPhoneDestinationVanilla> {
            HintPhoneNumberScreen(onBackClick = navigator::goBack)
        }

        composable<GraphVanilla.BottomTab.Demo.MovieDestinationVanilla> {
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

        composable<GraphVanilla.BottomTab.Demo.DialogDestinationVanilla> {
            val viewModel = koinViewModel<DialogDemoVM>()
            DialogDemoScreen(
                viewModel = viewModel,
                onBackClick = navigator::goBack
            )
        }
    }
}
