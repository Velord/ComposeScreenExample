package com.velord.feature.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.feature.movie.component.MovieAction
import com.velord.feature.movie.component.MovieHeader
import com.velord.feature.movie.component.MoviePager
import com.velord.feature.movie.component.MovieSortAndFilter
import com.velord.feature.movie.model.MovieFilterOptionUI
import com.velord.feature.movie.model.MovieSortOptionUI
import com.velord.feature.movie.viewModel.MovieUiState
import com.velord.feature.movie.viewModel.MovieViewModel
import com.velord.uicore.compose.preview.PreviewCombined

@Composable
fun MovieScreen(viewModel: MovieViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        uiState = uiState.value,
        onSwipe = viewModel::onSwipe,
        onSortOptionClick = viewModel::onSortOptionClick,
        onFilterOptionClick = viewModel::onFilterOptionClick,
    )
}

@Composable
private fun Content(
    uiState: MovieUiState,
    onSwipe: (Int) -> Unit,
    onSortOptionClick: (MovieSortOptionUI) -> Unit,
    onFilterOptionClick: (MovieFilterOptionUI) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                MovieHeader(
                    currentPage = uiState.currentPage,
                    pages = uiState.pageRoster,
                    onClick = onSwipe
                )

                MoviePager(
                    uiState = uiState,
                    onSwipe = onSwipe
                )
            }

            val showSortBottomSheetState = remember { mutableStateOf(false) }
            val showFilterBottomSheetState = remember { mutableStateOf(false) }

            MovieAction(
                onSortClick = {
                    showSortBottomSheetState.value = true
                },
                onFilterClick = {
                    showFilterBottomSheetState.value = true
                },
            )

            MovieSortAndFilter(
                uiState = uiState,
                isSortShowing = showSortBottomSheetState.value,
                isFilterShowing = showFilterBottomSheetState.value,
                onHideSort = { showSortBottomSheetState.value = false },
                onHideFilter = { showFilterBottomSheetState.value = false },
                onSortOptionClick = onSortOptionClick,
                onFilterOptionClick = onFilterOptionClick
            )
        }
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    Content(
        uiState = MovieUiState.DEFAULT,
        onSwipe = {},
        onSortOptionClick = {},
        onFilterOptionClick = {},
    )
}