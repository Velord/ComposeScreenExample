package com.velord.feature.movie.viewModel

import android.util.Log
import com.velord.model.movie.Movie
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.movie.GetFavoriteMovieUC
import com.velord.usecase.movie.UpdateMovieLikeUC
import com.velord.usecase.movie.result.GetFavoriteMovieResult
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
            val newError = when(result) {
                is UpdateMovieResult.Success -> null
                is UpdateMovieResult.DbError -> result.message
            }
            uiState.value = uiState.value.copy(error = newError)
        }
    }

    private fun observe() {
        launch {
            val result = getFavoriteMovieUC()
            val newError = when(result) {
                is GetFavoriteMovieResult.Success -> null
                is GetFavoriteMovieResult.MergeError -> result.message
            }
            uiState.value = uiState.value.copy(error = newError)
            Log.d("@@@", "getFavoriteMovieUC: $result")
            when(result) {
                is GetFavoriteMovieResult.Success -> result.flow
                is GetFavoriteMovieResult.MergeError -> null
            }?.collect { roster ->
                uiState.value = uiState.value.copy(roster = roster)
            }
        }
    }
}