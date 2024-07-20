package com.velord.feature.movie.viewModel

import com.velord.feature.movie.model.MovieFilterOptionUI
import com.velord.feature.movie.model.MoviePage
import com.velord.feature.movie.model.MovieSortOptionUI
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.movie.GetMovieSortOptionUC
import com.velord.usecase.movie.SetMovieSortOptionUC
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

    fun getSelectedSortOption(): MovieSortOptionUI? =
        sortOptionRoster.firstOrNull { it.isSelected }

    companion object {
        val DEFAULT: MovieUiState = MovieUiState(
            initialPage = 0,
            pageCount = 2,
            currentPage = 0,
            sortOptionRoster = listOf(),
            movieFilterOptionRoster = listOf()
        )
    }
}

class MovieViewModel(
    private val getMovieSortOptionUC: GetMovieSortOptionUC,
    private val setMovieSortOptionUC: SetMovieSortOptionUC
) : CoroutineScopeViewModel() {

    val uiState: MutableStateFlow<MovieUiState> = MutableStateFlow(MovieUiState.DEFAULT)

    init {
        observe()
    }

    fun onSwipe(newPage: Int) {
        uiState.update {
            it.copy(currentPage = newPage)
        }
    }

    fun onSortOptionClick(newOption: MovieSortOptionUI) {
        if (newOption.isSelected) return

        val domain = newOption.toDomain()
        setMovieSortOptionUC(domain)
    }

    fun onFilterOptionClick(newOption: MovieFilterOptionUI) {
        // TODO
    }

    private fun observe() {
        launch {
            getMovieSortOptionUC().collect { newValue ->
                uiState.update { state ->
                    state.copy(sortOptionRoster = newValue.map { MovieSortOptionUI.fromDomain(it) })
                }
            }
        }
    }
}