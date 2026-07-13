package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.movie.MovieScreen
import com.velord.ui.feature.movie.viewModel.AllMovieVM
import com.velord.ui.feature.movie.viewModel.FavoriteMovieVM
import com.velord.ui.feature.movie.viewModel.MovieVM
import org.koin.compose.viewmodel.koinViewModel

internal object MovieVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<MovieVM>()
        val allMovieVM = koinViewModel<AllMovieVM>()
        val favoriteMovieVM = koinViewModel<FavoriteMovieVM>()
        MovieScreen(viewModel, allMovieVM, favoriteMovieVM)
    }
}
