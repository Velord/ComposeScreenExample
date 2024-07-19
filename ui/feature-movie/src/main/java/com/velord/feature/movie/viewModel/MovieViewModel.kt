package com.velord.feature.movie.viewModel

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.velord.model.movie.FilterOption
import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import com.velord.resource.R
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.usecase.movie.GetMovieSortOptionUC
import com.velord.usecase.movie.SetMovieSortOptionUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class MoviePage(
    @StringRes val titleRes: Int,
    val imageRes: ImageVector
) {
    All(
        titleRes = com.velord.resource.R.string.all,
        imageRes = Icons.Rounded.List
    ),
    Favorite(
        titleRes = com.velord.resource.R.string.favorite,
        imageRes = Icons.Rounded.Favorite
    )
}

data class MovieUiState(
    val initialPage: Int,
    val pageCount: Int,
    val currentPage: Int,
    val sortOptionRoster: List<MovieSortOption>,
    val filterOptionRoster: List<FilterOption>
) {
    val pageRoster = MoviePage.entries

    companion object {
        val DEFAULT: MovieUiState = MovieUiState(
            initialPage = 0,
            pageCount = 2,
            currentPage = 0,
            sortOptionRoster = listOf(
                MovieSortOption(SortType.DateDescending, isSelected = true),
                MovieSortOption(SortType.DateAscending, isSelected = false),
            ),
            filterOptionRoster = listOf(
                FilterOption(R.string.filter_by_title_over_10_char, isSelected = false),
                FilterOption(R.string.filter_by_description_over_50_char, isSelected = false),
            )
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

    fun onSortOptionClick(newOption: MovieSortOption) {
        if (newOption.isSelected) return

        setMovieSortOptionUC(newOption)
    }

    fun onFilterOptionClick(newOption: FilterOption) {
        // TODO
    }

    private fun observe() {
        launch {
            getMovieSortOptionUC().collect { newValue ->
                uiState.update {
                    it.copy(sortOptionRoster = newValue)
                }
            }
        }
    }
}