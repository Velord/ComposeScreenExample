package com.velord.infrastructure.navigation.compose.nav3.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3
import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationDestinationsVM
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
import org.koin.androidx.compose.koinViewModel

internal fun EntryProviderScope<NavKey>.setupDemoGraphNav3(navigator: DemoNavigator) {
    entry<GraphNav3.BottomTab.Demo.DemoDestinationNav3> {
        val viewModel = koinViewModel<DemoVM>()
        val bottomNavVM = koinViewModel<BottomNavigationDestinationsVM>()

        DemoScreen(
            viewModel = viewModel,
            onNavigationEvent = {
                navigator.goTo(it)
            },
            onBackClick = {
                // To enable System Back Button handling
                // via Bottom Navigation -> comment the line below
                // bottomNavViewModel.graphCompletedHandling()
                bottomNavVM.graphTakeResponsibility()
            },
        )
    }

    entry<GraphNav3.BottomTab.Demo.ShapeDemoDestinationNav3> {
        ShapeDemoScreen()
    }

    entry<GraphNav3.BottomTab.Demo.ModifierDestinationNav3> {
        ModifierDemoScreen()
    }

    entry<GraphNav3.BottomTab.Demo.FlowSummatorDestinationNav3> {
        val viewModel = koinViewModel<FlowSummatorVM>()
        FlowSummatorScreen(viewModel)
    }

    entry<GraphNav3.BottomTab.Demo.MorphDemoDestinationNav3> {
        MorphDemoScreen()
    }

    entry<GraphNav3.BottomTab.Demo.HintPhoneDestinationNav3> {
        HintPhoneNumberScreen()
    }

    entry<GraphNav3.BottomTab.Demo.MovieDestinationNav3> {
        val viewModel = koinViewModel<MovieVM>()
        val allMovieVM = koinViewModel<AllMovieVM>()
        val favoriteMovieVM = koinViewModel<FavoriteMovieVM>()
        MovieScreen(viewModel, allMovieVM, favoriteMovieVM)
    }

    entry<GraphNav3.BottomTab.Demo.DialogDestinationNav3> {
        val viewModel = koinViewModel<DialogDemoVM>()
        DialogDemoScreen(viewModel)
    }
}
