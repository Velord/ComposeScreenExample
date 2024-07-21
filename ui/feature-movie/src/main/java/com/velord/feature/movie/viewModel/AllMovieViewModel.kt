package com.velord.feature.movie.viewModel

import android.util.Log
import com.velord.model.movie.Movie
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.movie.GetAllMovieUC
import com.velord.usecase.movie.GetMovieResult
import com.velord.usecase.movie.LoadNewPageMovieUC
import com.velord.usecase.movie.MovieResult
import com.velord.usecase.movie.RefreshMovieUC
import com.velord.usecase.movie.UpdateMovieLikeUC
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class AllMovieUiState(
    val roster: List<Movie>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val error: String?
) {
    companion object {
        val DEFAULT: AllMovieUiState = AllMovieUiState(
            roster = emptyList(),
            isLoading = false,
            isRefreshing = false,
            error = null
        )
    }
}

private const val INIT_INDEX = -1

sealed class PaginationStatus  {
    data object Init: PaginationStatus()
    data class Trigger(val index: Int): PaginationStatus()
    data object Exausted : PaginationStatus()
}

class AllMovieViewModel(
    private val getAllMovieUC: GetAllMovieUC,
    private val updateMovieLikeUC: UpdateMovieLikeUC,
    private val loadNewPageMovieUC: LoadNewPageMovieUC,
    private val refreshMovieUC: RefreshMovieUC
) : CoroutineScopeViewModel() {

    val uiState: MutableStateFlow<AllMovieUiState> = MutableStateFlow(AllMovieUiState.DEFAULT)

    private val paginationStatusFlow = MutableStateFlow<PaginationStatus>(PaginationStatus.Init)

    init {
        observe()
    }

    fun onLikeClick(movie: Movie) {
        updateMovieLikeUC(movie)
    }

    fun onEndList(triggerIndex: Int) {
        if (uiState.value.isLoading) return
        if (uiState.value.isRefreshing) return
        // TODO: Add Additional logic
        // if (lastVisibleIndex < uiState.value.roster.lastIndex) return
//
//        val lastIndex = uiState.value.roster.lastIndex
//        val isEndList = lastListIndexFlow.value == lastIndex
//        if (isEndList.not()) {
//            lastListIndexFlow.tryEmit(lastIndex)
//        }
        paginationStatusFlow.value = PaginationStatus.Trigger(triggerIndex)
    }

    fun onRefresh() {
        if (uiState.value.isRefreshing) return

        paginationStatusFlow.value = PaginationStatus.Init
        launch {
            uiState.value = uiState.value.copy(isRefreshing = true)
            when(refreshMovieUC()) {
                MovieResult.Success -> uiState.value = uiState.value.copy(error = null)
                is MovieResult.LoadPageFailed -> {
                    uiState.value = uiState.value.copy(error = "Error")
                    paginationStatusFlow.value = PaginationStatus.Init
                }
            }
        }.invokeOnCompletion {
            uiState.value = uiState.value.copy(isRefreshing = false)
        }
    }

    @OptIn(FlowPreview::class)
    private fun observe() {
        launch {
            val result = getAllMovieUC()
            when(result) {
                is GetMovieResult.Success -> uiState.value = uiState.value.copy(error = null)
                is GetMovieResult.DBError -> uiState.value = uiState.value.copy(error = "Error")
            }
            result.flow.collect { roster ->
                uiState.value = uiState.value.copy(roster = roster)
            }
        }

        launch {
            paginationStatusFlow
                .onEach {
                    Log.d("@@@", "paginationStatusFlow: $it")
                }
                .filter { it != PaginationStatus.Init }
                .filter { it !is PaginationStatus.Exausted }
                .filter {
//                    if (it is PaginationStatus.Trigger) {
//                        it.index > 0
//                    } else {
//                        true
//                    }

                    true
                }
                .debounce(300)
                .collect {
                    Log.d("@@@", "loadNewPageMovieUC: $it")
                    loadNewPage()
                }
        }
    }

    private fun loadNewPage() {
        launch {
            uiState.value = uiState.value.copy(isLoading = true)
            when(loadNewPageMovieUC()) {
                MovieResult.Success -> uiState.value = uiState.value.copy(error = null)
                is MovieResult.LoadPageFailed -> {
                    uiState.value = uiState.value.copy(error = "Error")
                    paginationStatusFlow.value = PaginationStatus.Init
                }
            }
        }.invokeOnCompletion {
            uiState.value = uiState.value.copy(isLoading = false)
        }
    }
}