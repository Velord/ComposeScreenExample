package com.velord.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.feature.movie.MovieScreen
import com.velord.feature.movie.viewModel.AllMovieViewModel
import com.velord.feature.movie.viewModel.FavoriteMovieViewModel
import com.velord.feature.movie.viewModel.MovieViewModel
import org.koin.androidx.compose.koinViewModel

object MovieVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<MovieViewModel>()
        val allMovieViewModel = koinViewModel<AllMovieViewModel>()
        val favoriteMovieViewModel = koinViewModel<FavoriteMovieViewModel>()
        MovieScreen(viewModel, allMovieViewModel, favoriteMovieViewModel)
    }
}