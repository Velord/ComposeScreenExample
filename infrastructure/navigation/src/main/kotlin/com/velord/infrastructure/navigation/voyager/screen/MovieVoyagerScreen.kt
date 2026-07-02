package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.movie.MovieScreen
import com.velord.ui.feature.movie.viewModel.AllMovieViewModel
import com.velord.ui.feature.movie.viewModel.FavoriteMovieViewModel
import com.velord.ui.feature.movie.viewModel.MovieViewModel
import org.koin.androidx.compose.koinViewModel

internal object MovieVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<MovieViewModel>()
        val allMovieViewModel = koinViewModel<AllMovieViewModel>()
        val favoriteMovieViewModel = koinViewModel<FavoriteMovieViewModel>()
        MovieScreen(viewModel, allMovieViewModel, favoriteMovieViewModel)
    }
}
