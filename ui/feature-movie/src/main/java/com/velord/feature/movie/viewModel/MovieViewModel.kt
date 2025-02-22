package com.velord.feature.movie.viewModel

import com.velord.feature.movie.model.MovieFilterOptionUI
import com.velord.feature.movie.model.MoviePage
import com.velord.feature.movie.model.MovieSortOptionUI
import com.velord.model.movie.MovieFilterOption
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.movie.GetMovieSortOptionUC
import com.velord.usecase.movie.SetMovieSortOptionUC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// TODO: Create Filter logic through layers. Use Default by now

data class MovieUiState(
    val initialPage: Int,
    val pageCount: Int,
    val currentPage: Int,
    val sortOptionRoster: List<MovieSortOptionUI>,
    val movieFilterOptionRoster: List<MovieFilterOptionUI>
) {
    val pageRoster = MoviePage.entries

    fun getSelectedSortOption(): MovieSortOptionUI? =
        sortOptionRoster.firstOrNull { it.isSelected }

    companion object {
        val DEFAULT: MovieUiState = MovieUiState(
            initialPage = 0,
            pageCount = 2,
            currentPage = 0,
            sortOptionRoster = listOf(),
            movieFilterOptionRoster = MovieFilterOption.createAll().map { MovieFilterOptionUI.fromDomain(it) }
        )
    }
}

sealed interface MovieUiAction {
    data class PageSwipe(val newPage: Int) : MovieUiAction
    data class SortOptionClick(val newOption: MovieSortOptionUI) : MovieUiAction
    data class FilterOptionClick(val newOption: MovieFilterOptionUI) : MovieUiAction
}

class MovieViewModel(
    private val getMovieSortOptionUC: GetMovieSortOptionUC,
    private val setMovieSortOptionUC: SetMovieSortOptionUC
) : CoroutineScopeViewModel() {

    val uiState: MutableStateFlow<MovieUiState> = MutableStateFlow(MovieUiState.DEFAULT)
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
        uiState.update {
            it.copy(currentPage = newPage)
        }
    }

    private fun onSortOptionClick(newOption: MovieSortOptionUI) {
        if (newOption.isSelected) return

        val domain = newOption.toDomain()
        setMovieSortOptionUC(domain)
    }

    private fun onFilterOptionClick(newOption: MovieFilterOptionUI) {
        // TODO: disabled for now
    }

    private fun observe() {
        launch {
            getMovieSortOptionUC().collect { newValue ->
                uiState.update { state ->
                    state.copy(sortOptionRoster = newValue.map { MovieSortOptionUI.fromDomain(it) })
                }
            }
        }
        launch {
            actionFlow.collect { action ->
                when (action) {
                    is MovieUiAction.PageSwipe -> onPageSwipe(action.newPage)
                    is MovieUiAction.SortOptionClick -> onSortOptionClick(action.newOption)
                    is MovieUiAction.FilterOptionClick -> onFilterOptionClick(action.newOption)
                }
            }
        }
    }
}