package com.velord.navigation.compose.nav3.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
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
import com.velord.navigation.compose.nav3.GraphNav3
import com.velord.shapedemo.ShapeDemoScreen
import org.koin.androidx.compose.koinViewModel

internal fun EntryProviderScope<NavKey>.setupDemoGraphNav3(navigator: DemoNavigator) {
    entry<GraphNav3.BottomTab.Demo.DemoDestinationNav3> {
        val viewModel = koinViewModel<DemoViewModel>()
        DemoScreen(viewModel) {
            navigator.goTo(it)
        }
    }

    entry<GraphNav3.BottomTab.Demo.ShapeDemoDestinationNav3> {
        ShapeDemoScreen()
    }

    entry<GraphNav3.BottomTab.Demo.ModifierDestinationNav3> {
        ModifierDemoScreen()
    }

    entry<GraphNav3.BottomTab.Demo.FlowSummatorDestinationNav3> {
        val viewModel = koinViewModel<FlowSummatorViewModel>()
        FlowSummatorScreen(viewModel)
    }

    entry<GraphNav3.BottomTab.Demo.MorphDemoDestinationNav3> {
        MorphDemoScreen()
    }

    entry<GraphNav3.BottomTab.Demo.HintPhoneDestinationNav3> {
        HintPhoneNumberScreen()
    }

    entry<GraphNav3.BottomTab.Demo.MovieDestinationNav3> {
        val viewModel = koinViewModel<MovieViewModel>()
        val allMovieViewModel = koinViewModel<AllMovieViewModel>()
        val favoriteMovieViewModel = koinViewModel<FavoriteMovieViewModel>()
        MovieScreen(viewModel, allMovieViewModel, favoriteMovieViewModel)
    }

    entry<GraphNav3.BottomTab.Demo.DialogDestinationNav3> {
        val viewModel = koinViewModel<DialogDemoViewModel>()
        DialogDemoScreen(viewModel)
    }
}