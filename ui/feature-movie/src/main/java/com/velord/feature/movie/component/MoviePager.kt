package com.velord.feature.movie.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.feature.movie.viewModel.AllMovieViewModel
import com.velord.feature.movie.viewModel.FavoriteMovieViewModel
import com.velord.feature.movie.viewModel.MovieUiState
import com.velord.model.movie.Movie
import com.velord.model.movie.SortType
import com.velord.uicore.compose.preview.PreviewCombined
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@Composable
internal fun ColumnScope.MoviePager(
    uiState: MovieUiState,
    onSwipe: (Int) -> Unit
) {
    val allMovieViewModel = koinViewModel<AllMovieViewModel>()
    val favoriteMovieViewModel = koinViewModel<FavoriteMovieViewModel>()

    val allMovieUiState = allMovieViewModel.uiState.collectAsStateWithLifecycle()
    val favoriteMovieUiState = favoriteMovieViewModel.uiState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(
        initialPage = uiState.initialPage,
        pageCount = { uiState.pageCount },
    )
    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            onSwipe(it)
        }
    }
    LaunchedEffect(key1 = uiState.currentPage) {
        if (pagerState.currentPage != uiState.currentPage) {
            pagerState.animateScrollToPage(uiState.currentPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        beyondViewportPageCount = 1
    ) { index ->
        when(index) {
            0 -> RefreshPage(
                roster = allMovieUiState.value.roster,
                selectedSortOption = uiState.getSelectedSortOption()?.type,
                onLike = allMovieViewModel::onLikeClick,
                isDataExausted = allMovieUiState.value.paginationStatus.isExausted,
                isPaginationAvailable = allMovieUiState.value.isPaginationAvailable,
                onEndList = allMovieViewModel::onEndList,
                isRefreshing = allMovieUiState.value.isRefreshing,
                onRefresh = allMovieViewModel::onRefresh
            )
            1 -> RefreshPage(
                roster = favoriteMovieUiState.value.roster,
                selectedSortOption = uiState.getSelectedSortOption()?.type,
                onLike = favoriteMovieViewModel::onLikeClick
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RefreshPage(
    roster: List<Movie>,
    selectedSortOption: SortType?,
    onLike: (Movie) -> Unit,
    isDataExausted: Boolean = false,
    isPaginationAvailable: Boolean = false,
    onEndList: (lastVisibleIndex: Int) -> Unit = {},
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {}
) {
    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState, enabled = isPaginationAvailable)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isDataExausted) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = com.velord.resource.R.string.all_movies),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                            .shadow(
                                shape = RoundedCornerShape(16.dp),
                                elevation = 30.dp,
                                ambientColor = Color.Green,
                                spotColor = Color.Green
                            )
                        ,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            MoviePage(
                roster = roster,
                selectedSortOption = selectedSortOption,
                onLike = onLike,
                isPaginationAvailable = isPaginationAvailable,
                onEndList = onEndList
            )
        }

        if (isPaginationAvailable) {
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}


@PreviewCombined
@Composable
private fun MoviePagerPreview() {
    RefreshPage(
        roster = listOf(
            Movie(
                id = 1,
                title = "Star Wars",
                description = "A long time ago in a galaxy far, far away...",
                isLiked = true,
                date = Calendar.getInstance(),
                rating = 7.66f
            ),
            Movie(
                id = 2,
                title = "The Lord of the Rings",
                description = "One ring",
                isLiked = false,
                date = Calendar.getInstance(),
                rating = 7.66f
            ),
            Movie(
                id = 3,
                title = "Shawshank Redemption",
                description = "Two imprisoned",
                isLiked = true,
                date = Calendar.getInstance(),
                rating = 7.66f
            ),
            Movie(
                id = 4,
                title = "The Godfather",
                description = "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
                isLiked = false,
                date = Calendar.getInstance(),
                rating = 7.66f
            ),
            Movie(
                id = 5,
                title = "The Dark Knight",
                description = "When the menace known as the Joker wreaks havoc and chaos on the",
                isLiked = true,
                date = Calendar.getInstance(),
                rating = 7.66f
            ),
            Movie(
                id = 6,
                title = "The Matrix",
                description = "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
                isLiked = false,
                date = Calendar.getInstance(),
                rating = 7.66f
            ),
        ),
        selectedSortOption = SortType.DateAscending,
        onLike = {},
        isDataExausted = true
    )
}