package com.velord.feature.movie.viewModel

import com.velord.model.movie.Movie
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.movie.GetFavoriteMovieUC
import com.velord.usecase.movie.UpdateMovieLikeUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

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

    val uiStateFlow: MutableStateFlow<FavoriteMovieUiState> = MutableStateFlow(FavoriteMovieUiState.DEFAULT)
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
            try {
                updateMovieLikeUC(movie)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                uiStateFlow.value = uiStateFlow.value.copy(error = e.message)
            }
        }
    }

    private fun observe() {
        launch {
            try {
                getFavoriteMovieUC().flow.collect { roster ->
                    uiStateFlow.value = uiStateFlow.value.copy(roster = roster)
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                uiStateFlow.value = uiStateFlow.value.copy(error = e.message)
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
