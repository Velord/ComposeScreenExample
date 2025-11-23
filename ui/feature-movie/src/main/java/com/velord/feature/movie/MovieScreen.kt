package com.velord.feature.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.core.ui.compose.preview.PreviewCombined
import com.velord.feature.movie.component.MovieAction
import com.velord.feature.movie.component.MovieBottomSheet
import com.velord.feature.movie.component.MovieHeader
import com.velord.feature.movie.component.MoviePager
import com.velord.feature.movie.viewModel.AllMovieViewModel
import com.velord.feature.movie.viewModel.FavoriteMovieViewModel
import com.velord.feature.movie.viewModel.MovieUiAction
import com.velord.feature.movie.viewModel.MovieUiState
import com.velord.feature.movie.viewModel.MovieViewModel

@Composable
fun MovieScreen(
    viewModel: MovieViewModel,
    allMovieViewModel: AllMovieViewModel,
    favoriteMovieViewModel: FavoriteMovieViewModel,
) {
    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()

    Content(
        uiState = uiState.value,
        onAction = viewModel::onAction,
    ) {
        MoviePager(
            allMovieViewModel = allMovieViewModel,
            favoriteMovieViewModel = favoriteMovieViewModel,
            uiState = uiState.value,
            onSwipe = {
                viewModel.onAction(MovieUiAction.PageSwipe(it))
            }
        )
    }
}

@Composable
private fun Content(
    uiState: MovieUiState,
    onAction: (MovieUiAction) -> Unit,
    content: @Composable ColumnScope.() -> Unit,
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
                    onClick = { onAction(MovieUiAction.PageSwipe(it)) }
                )

                content()
            }

            FloatingAction(
                uiState = uiState,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun BoxScope.FloatingAction(
    uiState: MovieUiState,
    onAction: (MovieUiAction) -> Unit,
) {
    val showSortBottomSheetState = remember { mutableStateOf(false) }
    val showFilterBottomSheetState = remember { mutableStateOf(false) }
    val showInfoBottomSheetState = remember { mutableStateOf(false) }

    MovieAction(
        onSortClick = {
            showSortBottomSheetState.value = true
        },
        onFilterClick = {
            showFilterBottomSheetState.value = true
        },
        onInfoClick = {
            showInfoBottomSheetState.value = true
        }
    )

    MovieBottomSheet(
        uiState = uiState,
        onAction = onAction,
        isSortShowing = showSortBottomSheetState.value,
        isFilterShowing = showFilterBottomSheetState.value,
        isInfoShowing = showInfoBottomSheetState.value,
        onHideSort = { showSortBottomSheetState.value = false },
        onHideFilter = { showFilterBottomSheetState.value = false },
        onHideInfo = { showInfoBottomSheetState.value = false },
    )
}

@PreviewCombined
@Composable
private fun Preview() {
    Content(
        uiState = MovieUiState.DEFAULT,
        onAction = {},
        content = {}
    )
}