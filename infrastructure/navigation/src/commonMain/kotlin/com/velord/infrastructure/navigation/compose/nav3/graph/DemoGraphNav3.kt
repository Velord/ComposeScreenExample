package com.velord.infrastructure.navigation.compose.nav3.graph

import androidx.navigation3.runtime.EntryProviderScope
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3
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

internal fun EntryProviderScope<GraphNav3>.setupDemoGraphNav3(navigator: DemoNavigator) {
    entry<GraphNav3.BottomTab.Demo.DemoDestinationNav3> {
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

    entry<GraphNav3.BottomTab.Demo.ShapeDemoDestinationNav3> {
        ShapeDemoScreen(onBackClick = navigator::goBack)
    }

    entry<GraphNav3.BottomTab.Demo.ModifierDestinationNav3> {
        ModifierDemoScreen(onBackClick = navigator::goBack)
    }

    entry<GraphNav3.BottomTab.Demo.FlowSummatorDestinationNav3> {
        val viewModel = koinViewModel<FlowSummatorVM>()
        FlowSummatorScreen(
            viewModel = viewModel,
            onBackClick = navigator::goBack
        )
    }

    entry<GraphNav3.BottomTab.Demo.MorphDemoDestinationNav3> {
        MorphDemoScreen(onBackClick = navigator::goBack)
    }

    entry<GraphNav3.BottomTab.Demo.HintPhoneDestinationNav3> {
        HintPhoneNumberScreen(onBackClick = navigator::goBack)
    }

    entry<GraphNav3.BottomTab.Demo.MovieDestinationNav3> {
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

    entry<GraphNav3.BottomTab.Demo.DialogDestinationNav3> {
        val viewModel = koinViewModel<DialogDemoVM>()
        DialogDemoScreen(
            viewModel = viewModel,
            onBackClick = navigator::goBack
        )
    }
}
