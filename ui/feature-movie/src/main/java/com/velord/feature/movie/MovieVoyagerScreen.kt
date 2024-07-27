package com.velord.feature.movie

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import com.velord.feature.movie.viewModel.MovieViewModel

object MovieVoyagerScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = viewModel<MovieViewModel>()
        MovieScreen(viewModel)
    }
}