package com.velord.feature.movie.viewModel

import com.velord.model.movie.Movie
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.movie.GetFavoriteMovieUC
import com.velord.usecase.movie.UpdateMovieLikeUC
import com.velord.usecase.movie.result.UpdateMovieResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class FavoriteMovieUiState(
    val roster: List<Movie>,
    val error: String?
) {
    companion object {
        val DEFAULT: FavoriteMovieUiState = FavoriteMovieUiState(
            roster = emptyList(),
            error = null
        )
    }
}

class FavoriteMovieViewModel(
    private val getFavoriteMovieUC: GetFavoriteMovieUC,
    private val updateMovieLikeUC: UpdateMovieLikeUC
) : CoroutineScopeViewModel() {

    val uiState: MutableStateFlow<FavoriteMovieUiState> = MutableStateFlow(FavoriteMovieUiState.DEFAULT)

    init {
        observe()
    }

    fun onLikeClick(movie: Movie) {
        launch {
            val result = updateMovieLikeUC(movie)
            when(result) {
                is UpdateMovieResult.Success -> uiState.value = uiState.value.copy(error = null)
                is UpdateMovieResult.DbError -> uiState.value = uiState.value.copy(error = "Error")
            }
        }
    }

    private fun observe() {
        launch {
            getFavoriteMovieUC().collect { roster ->
                uiState.value = uiState.value.copy(roster = roster)
            }
        }
    }
}