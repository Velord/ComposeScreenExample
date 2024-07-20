package com.velord.feature.movie.viewModel

import android.util.Log
import com.velord.model.movie.Movie
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.movie.GetAllMovieUC
import com.velord.usecase.movie.LoadNewPageMovieUC
import com.velord.usecase.movie.RefreshMovieUC
import com.velord.usecase.movie.UpdateMovieLikeUC
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

data class AllMovieUiState(val roster: List<Movie>) {
    companion object {
        val DEFAULT: AllMovieUiState = AllMovieUiState(
            roster = emptyList()
        )
    }
}

private const val INIT_INDEX = -1

class AllMovieViewModel(
    private val getAllMovieUC: GetAllMovieUC,
    private val updateMovieLikeUC: UpdateMovieLikeUC,
    private val loadNewPageMovieUC: LoadNewPageMovieUC,
    private val refreshMovieUC: RefreshMovieUC
) : CoroutineScopeViewModel() {

    val uiState: MutableStateFlow<AllMovieUiState> = MutableStateFlow(AllMovieUiState.DEFAULT)

    private val lastListIndexFlow = MutableStateFlow(INIT_INDEX)

    init {
        observe()
    }

    fun onLikeClick(movie: Movie) {
        updateMovieLikeUC(movie)
    }

    fun onEndList(lastVisibleIndex: Int) {
        if (lastVisibleIndex != uiState.value.roster.lastIndex) return

        val lastIndex = uiState.value.roster.lastIndex
        val isEndList = lastListIndexFlow.value == lastIndex
        if (isEndList.not()) {
            lastListIndexFlow.tryEmit(lastIndex)
        }
    }

    fun onRefresh() {
        refreshMovieUC()
    }

    @OptIn(FlowPreview::class)
    private fun observe() {
        launch {
            getAllMovieUC().collect { roster ->
                uiState.value = AllMovieUiState(roster)
            }
        }

        launch {
            lastListIndexFlow
                .filter { it != INIT_INDEX }
                .filter { it > 0 }
                .debounce(300)
                .collect {
                    Log.d("@@@", "loadNewPageMovieUC: $it")
                    loadNewPageMovieUC()
                }
        }
    }
}