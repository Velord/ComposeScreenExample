package com.velord.ui.feature.movie.viewModel

import com.velord.model.movie.MovieFilterOption
import com.velord.ui.feature.movie.model.MovieFilterOptionUI
import com.velord.ui.feature.movie.model.MoviePage
import com.velord.ui.feature.movie.model.MovieSortOptionUI
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import com.velord.usecase.movie.GetMovieFilterOptionUC
import com.velord.usecase.movie.GetMovieSortOptionUC
import com.velord.usecase.movie.SetMovieFilterOptionUC
import com.velord.usecase.movie.SetMovieSortOptionUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MovieUiState(
    val initialPage: Int,
    val pageCount: Int,
    val currentPage: Int,
    val sortOptionRoster: List<MovieSortOptionUI>,
    val movieFilterOptionRoster: List<MovieFilterOptionUI>
) {
    val pageRoster = MoviePage.entries

    fun getSelectedSortOption(): MovieSortOptionUI? = sortOptionRoster.firstOrNull { it.isSelected }

    companion object {
        val DEFAULT: MovieUiState = MovieUiState(
            initialPage = 0,
            pageCount = 2,
            currentPage = 0,
            sortOptionRoster = listOf(),
            movieFilterOptionRoster = MovieFilterOption.ALL.map {
                MovieFilterOptionUI.fromDomain(it)
            }
        )
    }
}

sealed interface MovieUiAction {
    data class PageSwipe(val newPage: Int) : MovieUiAction
    data class SortOptionClick(val newOption: MovieSortOptionUI) : MovieUiAction
    data class FilterOptionChange(val newOption: MovieFilterOption) : MovieUiAction
}

class MovieVM(
    private val getMovieSortOptionUC: GetMovieSortOptionUC,
    private val setMovieSortOptionUC: SetMovieSortOptionUC,
    private val getMovieFilterOptionUC: GetMovieFilterOptionUC,
    private val setMovieFilterOptionUC: SetMovieFilterOptionUC
) : CoroutineScopeVM() {

    val uiStateFlow: MutableStateFlow<MovieUiState> = MutableStateFlow(MovieUiState.DEFAULT)
    private val actionFlow = MutableSharedFlow<MovieUiAction>()

    init {
        observe()
    }

    fun onAction(action: MovieUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onPageSwipe(newPage: Int) {
        uiStateFlow.update {
            it.copy(currentPage = newPage)
        }
    }

    private fun onSortOptionClick(newOption: MovieSortOptionUI) {
        if (newOption.isSelected) return

        val domain = newOption.toDomain()
        setMovieSortOptionUC(domain)
    }

    private fun onFilterOptionChange(newOption: MovieFilterOption) {
        launch {
            setMovieFilterOptionUC(newOption)
        }
    }

    private fun observe() {
        launch {
            getMovieSortOptionUC().flow.collect { newValue ->
                uiStateFlow.update { state ->
                    state.copy(sortOptionRoster = newValue.map { MovieSortOptionUI.fromDomain(it) })
                }
            }
        }
        launch {
            getMovieFilterOptionUC().flow.collect { newValue ->
                uiStateFlow.update { state ->
                    state.copy(movieFilterOptionRoster = newValue.map { MovieFilterOptionUI.fromDomain(it) })
                }
            }
        }
        launch {
            actionFlow.collect { action ->
                when (action) {
                    is MovieUiAction.PageSwipe -> onPageSwipe(action.newPage)
                    is MovieUiAction.SortOptionClick -> onSortOptionClick(action.newOption)
                    is MovieUiAction.FilterOptionChange -> onFilterOptionChange(action.newOption)
                }
            }
        }
    }
}
