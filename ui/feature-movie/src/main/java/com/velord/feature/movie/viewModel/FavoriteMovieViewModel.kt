package com.velord.feature.movie.viewModel

import com.velord.model.movie.Movie
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.movie.GetFavoriteMovieUC
import com.velord.usecase.movie.UpdateMovieLikeUC
import com.velord.usecase.movie.result.GetFavoriteMovieResult
import com.velord.usecase.movie.result.UpdateMovieResult
import kotlinx.coroutines.flow.MutableSharedFlow
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

sealed interface FavoriteMovieUiAction {
    data class OnLikeClick(val movie: Movie) : FavoriteMovieUiAction
}

class FavoriteMovieViewModel(
    private val getFavoriteMovieUC: GetFavoriteMovieUC,
    private val updateMovieLikeUC: UpdateMovieLikeUC
) : CoroutineScopeViewModel() {

    val uiState: MutableStateFlow<FavoriteMovieUiState> = MutableStateFlow(FavoriteMovieUiState.DEFAULT)
    private val actionFlow = MutableSharedFlow<FavoriteMovieUiAction>()

    init {
        observe()
    }

    fun onAction(action: FavoriteMovieUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onLikeClick(movie: Movie) {
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

            when(result) {
                is GetFavoriteMovieResult.Success -> result.flow
                is GetFavoriteMovieResult.MergeError -> null
            }?.collect { roster ->
                uiState.value = uiState.value.copy(roster = roster)
            }
        }

        launch {
            actionFlow.collect { action ->
                when(action) {
                    is FavoriteMovieUiAction.OnLikeClick -> onLikeClick(action.movie)
                }
            }
        }
    }
}