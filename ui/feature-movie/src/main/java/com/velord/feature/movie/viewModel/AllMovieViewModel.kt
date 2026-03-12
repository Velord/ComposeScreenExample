package com.velord.feature.movie.viewModel

import android.content.Intent
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
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

sealed interface AllMovieUiAction {
    data class OnLikeClick(val movie: Movie) : AllMovieUiAction
    data class OnClick(val movie: Movie) : AllMovieUiAction
    data object OnRefresh : AllMovieUiAction
    data class OnEndList(val triggerIndex: Int) : AllMovieUiAction
}

class AllMovieViewModel(
    private val getAllMovieUC: GetAllMovieUC,
    private val updateMovieLikeUC: UpdateMovieLikeUC,
    private val loadNewPageMovieUC: LoadNewPageMovieUC,
    private val refreshMovieUC: RefreshMovieUC,
) : CoroutineScopeViewModel() {

    val uiStateFlow: MutableStateFlow<AllMovieUiState> = MutableStateFlow(AllMovieUiState.DEFAULT)
    val shareEvent = MutableSharedFlow<Intent>()
    private val actionFlow = MutableSharedFlow<AllMovieUiAction>()

    init {
        observe()
    }

    fun onAction(action: AllMovieUiAction) {
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
            uiStateFlow.value = uiStateFlow.value.copy(error = newError)
        }
    }

    private fun onEndList(triggerIndex: Int) {
        if (uiStateFlow.value.isLoadPageAvailable.not()) return
        uiStateFlow.update {
            it.copy(paginationStatus = PaginationStatus.Trigger(triggerIndex))
        }
    }

    private fun onRefresh() {
        if (uiStateFlow.value.isRefreshAvailable.not()) return

        uiStateFlow.update {
            it.copy(paginationStatus = PaginationStatus.Init)
        }
        launch {
            uiStateFlow.value = uiStateFlow.value.copy(isRefreshing = true)
            val result = refreshMovieUC()
            result.handleLoadPageResult()
            uiStateFlow.value = uiStateFlow.value.copy(isRefreshing = false)
        }
    }

    private fun onClick(movie: Movie) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, movie.toString())
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        launch {
            shareEvent.emit(shareIntent)
        }
    }

    @OptIn(FlowPreview::class)
    private fun observe() {
        launch {
            val result = getAllMovieUC()
            val newError = when(result) {
                is GetMovieResult.Success -> null
                is GetMovieResult.DBError -> result.message
                is GetMovieResult.MergeError -> result.message
            }
            uiStateFlow.value = uiStateFlow.value.copy(error = newError)

            when(result) {
                is GetMovieResult.Success -> result.flow
                is GetMovieResult.DBError -> result.flow
                is GetMovieResult.MergeError -> null
            }?.collect { roster ->
                uiStateFlow.value = uiStateFlow.value.copy(roster = roster)
            }
        }

        launch {
            uiStateFlow
                .map { it.paginationStatus }
                .distinctUntilChanged()
                .filter { it != PaginationStatus.Init }
                .filter { it !is PaginationStatus.Exausted }
                .debounce(300)
                .collect {
                    loadNewPage()
                }
        }

        launch {
            actionFlow.collect { action ->
                when(action) {
                    is AllMovieUiAction.OnLikeClick -> onLikeClick(action.movie)
                    is AllMovieUiAction.OnClick -> onClick(action.movie)
                    is AllMovieUiAction.OnRefresh -> onRefresh()
                    is AllMovieUiAction.OnEndList -> onEndList(action.triggerIndex)
                }
            }
        }
    }

    private fun loadNewPage() = launch {
        uiStateFlow.value = uiStateFlow.value.copy(isLoading = true)
        val result = loadNewPageMovieUC()
        result.handleLoadPageResult()
        uiStateFlow.value = uiStateFlow.value.copy(isLoading = false)
    }

    private fun MovieLoadNewPageResult.handleLoadPageResult() {
        when(this) {
            MovieLoadNewPageResult.Success -> uiStateFlow.value = uiStateFlow.value.copy(error = null)
            is MovieLoadNewPageResult.LoadPageFailed -> {
                uiStateFlow.update {
                    it.copy(
                        error = this.message,
                        paginationStatus = PaginationStatus.Init
                    )
                }
            }
            is MovieLoadNewPageResult.Exausted -> {
                uiStateFlow.update {
                    it.copy(
                        error = null,
                        paginationStatus = PaginationStatus.Exausted
                    )
                }
            }
        }
    }
}