package com.velord.feature.movie.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.feature.movie.model.MovieSortOptionUI
import com.velord.feature.movie.viewModel.AllMovieViewModel
import com.velord.feature.movie.viewModel.FavoriteMovieViewModel
import com.velord.feature.movie.viewModel.MovieUiState
import com.velord.model.movie.Movie
import com.velord.uicore.compose.preview.PreviewCombined
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

private const val PRELOAD_BEFORE_END = 10

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
            .weight(1f)
    ) { index ->
        when(index) {
            0 -> Page(
                roster = allMovieUiState.value.roster,
                selectedSortOption = uiState.getSelectedSortOption(),
                onLike = allMovieViewModel::onLikeClick,
                isPaginationAvailable = true,
                onEndList = allMovieViewModel::onEndList,
                isRefreshing = allMovieUiState.value.isRefreshing,
                onRefresh = allMovieViewModel::onRefresh
            )
            1 -> Page(
                roster = favoriteMovieUiState.value.roster,
                selectedSortOption = uiState.getSelectedSortOption(),
                onLike = favoriteMovieViewModel::onLikeClick
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Page(
    roster: List<Movie>,
    selectedSortOption: MovieSortOptionUI?,
    onLike: (Movie) -> Unit,
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
        Pager(
            roster = roster,
            selectedSortOption = selectedSortOption,
            onLike = onLike,
            isPaginationAvailable = isPaginationAvailable,
            onEndList = onEndList
        )

        if (isPaginationAvailable) {
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun Pager(
    roster: List<Movie>,
    selectedSortOption: MovieSortOptionUI?,
    onLike: (Movie) -> Unit,
    isPaginationAvailable: Boolean = false,
    onEndList: (lastVisibleIndex: Int) -> Unit = {},
) {
    val pagerState = rememberLazyListState()
    val sortOptionState = remember {
        mutableStateOf(selectedSortOption)
    }
    val rosterSizeState = remember {
        mutableIntStateOf(roster.size)
    }
    rosterSizeState.intValue = roster.size

    val isAtBottomState = remember {
        derivedStateOf {
            val lastVisibleIndex = pagerState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
//            Log.d("@@@", "derivedStateOf: $lastVisibleIndex")
//            Log.d("@@@", "derivedStateOf Roster: ${rosterSizeState.value}")
            lastVisibleIndex >= rosterSizeState.intValue - PRELOAD_BEFORE_END
        }
    }
    Log.d("@@@", "isAtBottomState: ${isAtBottomState.value}")

    LaunchedEffect(isAtBottomState) {
        snapshotFlow { isAtBottomState.value }
            .filter { it }
            .collect {
                val lastVisibleIndex = pagerState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                Log.d("@@@", "onEndList: $lastVisibleIndex")
                onEndList(lastVisibleIndex)
            }
    }

    LaunchedEffect(key1 = selectedSortOption) {
        snapshotFlow { sortOptionState.value }
            .filter { it != selectedSortOption }
            .collect {
                sortOptionState.value = selectedSortOption
                delay(300)
                pagerState.scrollToItem(0)
            }
    }

    LazyColumn(state = pagerState) {
        itemsIndexed(
            items = roster,
            key = { _, item -> item.id }
        ) { index, item ->
            val prevItem = roster.getOrNull(index - 1)
            val isAnotherMonth = item.isAnotherMonthOrYear(prevItem?.date)
            if (index == 0 || isAnotherMonth) {
                MonthDivider(date = item.formattedDateForDivider)
            }

            MovieCard(
                modifier = Modifier.animateItem(),
                movie = item,
                onLike = { onLike(item) }
            )
        }

        if (isPaginationAvailable) {
            item("Pager CircularProgressIndicator") {
                AnimatedVisibility(visible = isAtBottomState.value && roster.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(64.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@PreviewCombined
@Composable
private fun MoviePagerPreview() {
    Page(
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
        selectedSortOption = MovieSortOptionUI.Default,
        onLike = {}
    )
}