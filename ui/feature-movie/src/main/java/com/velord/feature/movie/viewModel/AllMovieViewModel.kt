package com.velord.feature.movie.viewModel

import android.util.Log
import com.velord.model.movie.Movie
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.movie.GetAllMovieUC
import com.velord.usecase.movie.LoadNewPageMovieUC
import com.velord.usecase.movie.RefreshMovieUC
import com.velord.usecase.movie.UpdateMovieLikeUC
import com.velord.usecase.movie.result.GetMovieResult
import com.velord.usecase.movie.result.MovieLoadNewPageResult
import com.velord.usecase.movie.result.UpdateMovieResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AllMovieUiState(
    val roster: List<Movie>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val error: String?,
    val paginationStatus: PaginationStatus
) {
    companion object {
        val DEFAULT: AllMovieUiState = AllMovieUiState(
            roster = emptyList(),
            isLoading = false,
            isRefreshing = false,
            error = null,
            paginationStatus = PaginationStatus.Init
        )
    }

    val isRefreshAvailable: Boolean get() = isRefreshing.not() && paginationStatus.isExausted.not()
    val isLoadPageAvailable: Boolean get() = isLoading.not() && isRefreshAvailable
    val isPaginationAvailable: Boolean get() = paginationStatus.isExausted.not()
}

sealed class PaginationStatus  {

    val isExausted: Boolean get() = this is Exausted

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

    fun onEndList(triggerIndex: Int) {
        if (uiState.value.isLoadPageAvailable.not()) return
        // TODO: Add Additional logic
        // if (lastVisibleIndex < uiState.value.roster.lastIndex) return
//
//        val lastIndex = uiState.value.roster.lastIndex
//        val isEndList = lastListIndexFlow.value == lastIndex
//        if (isEndList.not()) {
//            lastListIndexFlow.tryEmit(lastIndex)
//        }
        uiState.update {
            it.copy(paginationStatus = PaginationStatus.Trigger(triggerIndex))
        }
    }

    fun onRefresh() {
        if (uiState.value.isRefreshAvailable.not()) return

        uiState.update {
            it.copy(paginationStatus = PaginationStatus.Init)
        }
        launch {
            uiState.value = uiState.value.copy(isRefreshing = true)
            val result = refreshMovieUC()
            result.handleLoadPageResult()
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
            uiState
                .map { it.paginationStatus }
                .distinctUntilChanged()
                .onEach {
                    Log.d("@@@", "paginationStatusFlow: $it")
                }
                .filter { it != PaginationStatus.Init }
                .filter { it !is PaginationStatus.Exausted }
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
            val result = loadNewPageMovieUC()
            result.handleLoadPageResult()
            uiState.value = uiState.value.copy(isLoading = false)
        }
    }

    private fun MovieLoadNewPageResult.handleLoadPageResult() {
        Log.d("@@@", "handleLoadPageResult: $this")
        when(this) {
            MovieLoadNewPageResult.Success -> uiState.value = uiState.value.copy(error = null)
            is MovieLoadNewPageResult.LoadPageFailed -> {
                uiState.update {
                    it.copy(
                        error = this.message,
                        paginationStatus = PaginationStatus.Init
                    )
                }
            }
            is MovieLoadNewPageResult.Exausted -> {
                uiState.update {
                    it.copy(
                        error = null,
                        paginationStatus = PaginationStatus.Exausted
                    )
                }
            }
        }
    }
}