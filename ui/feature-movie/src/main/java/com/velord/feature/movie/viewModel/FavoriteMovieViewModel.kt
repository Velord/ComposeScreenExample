package com.velord.feature.movie.viewModel

import com.velord.model.movie.Movie
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.movie.GetFavoriteMovieUC
import com.velord.usecase.movie.UpdateMovieLikeUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class FavoriteMovieUiState(val roster: List<Movie>) {
    companion object {
        val DEFAULT: FavoriteMovieUiState = FavoriteMovieUiState(
            roster = emptyList()
        )
    }
}

class FavoriteMovieViewModel(
    private val getFavoriteMovieUC: GetFavoriteMovieUC,
    private val updateMovieLikeUC: UpdateMovieLikeUC
) : CoroutineScopeViewModel() {

    val uiState: MutableStateFlow<FavoriteMovieUiState> = MutableStateFlow(FavoriteMovieUiState.DEFAULT)

    init {
        launch {
            getFavoriteMovieUC().collect { roster ->
                uiState.value = FavoriteMovieUiState(roster)
            }
        }
    }

    fun onLikeClick(movie: Movie) {
        updateMovieLikeUC(movie)
    }
}