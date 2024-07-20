package com.velord.feature.movie.viewModel

import com.velord.model.movie.Movie
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.movie.GetFavoriteMovieUC
import com.velord.usecase.movie.GetMovieResult
import com.velord.usecase.movie.UpdateMovieLikeUC
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
        launch {
            val result = getFavoriteMovieUC()
            when(result) {
                is GetMovieResult.Success -> uiState.value = uiState.value.copy(error = null)
                is GetMovieResult.DBError -> uiState.value = uiState.value.copy(error = "Error")
            }
            result.flow.collect { roster ->
                uiState.value = uiState.value.copy(roster = roster)
            }
        }
    }

    fun onLikeClick(movie: Movie) {
        updateMovieLikeUC(movie)
    }
}