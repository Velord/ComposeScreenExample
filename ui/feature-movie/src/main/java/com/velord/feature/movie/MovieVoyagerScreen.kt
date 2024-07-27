package com.velord.feature.movie

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.feature.movie.viewModel.MovieViewModel
import org.koin.androidx.compose.koinViewModel

object MovieVoyagerScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<MovieViewModel>()
        MovieScreen(viewModel)
    }
}