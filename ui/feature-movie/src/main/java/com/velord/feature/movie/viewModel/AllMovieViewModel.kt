package com.velord.feature.movie.viewModel

import android.util.Log
import com.velord.model.movie.Movie
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.movie.GetAllMovieUC
import com.velord.usecase.movie.UpdateMovieLikeUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class AllMovieUiState(val roster: List<Movie>) {
    companion object {
        val DEFAULT: AllMovieUiState = AllMovieUiState(
            roster = emptyList()
        )
    }
}

class AllMovieViewModel(
    private val getAllMovieUC: GetAllMovieUC,
    private val updateMovieLikeUC: UpdateMovieLikeUC
) : CoroutineScopeViewModel() {

    val uiState: MutableStateFlow<AllMovieUiState> = MutableStateFlow(AllMovieUiState.DEFAULT)

    init {
        launch {
            getAllMovieUC().collect { roster ->
                Log.d("@@@", "AllMovieViewModel $roster")
                uiState.value = AllMovieUiState(roster)
            }
        }
    }

    fun onLikeClick(movie: Movie) {
        updateMovieLikeUC(movie)
    }
}