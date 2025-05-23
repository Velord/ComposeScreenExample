package com.velord.feature.movie.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velord.model.movie.Movie
import com.velord.model.movie.MoviePagination
import com.velord.model.movie.SortType
import com.velord.model.movie.findRecentTimeInMilli
import com.velord.uicore.compose.preview.PreviewCombined
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSettings
import java.util.Calendar

private fun LazyListState.getLastVisibleIndex(): Int {
    return layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
}

@Composable
internal fun MoviePage(
    roster: List<Movie>,
    selectedSortOption: SortType?,
    onLike: (Movie) -> Unit,
    onClick: (Movie) -> Unit,
    isPaginationAvailable: Boolean = false,
    onEndList: (lastVisibleIndex: Int) -> Unit = {},
) {
    val listState = rememberLazyListState()
    val sortOptionState = remember {
        mutableStateOf(selectedSortOption)
    }
    val rosterSizeState = remember {
        mutableIntStateOf(roster.size)
    }
    val isAtBottomState = remember {
        derivedStateOf {
            Log.d("Pagination", "isAtBottomState: ${listState.getLastVisibleIndex()} ${rosterSizeState.intValue}")
            MoviePagination.shouldLoadMore(
                lastVisibleIndex = listState.getLastVisibleIndex(),
                totalItemCount = rosterSizeState.intValue
            )
        }
    }
    val mostRecentDateState: MutableState<Long?> = remember {
        mutableStateOf(roster.findRecentTimeInMilli())
    }

    LaunchedEffect(isAtBottomState) {
        snapshotFlow { isAtBottomState.value }
            .onEach { Log.d("Pagination", "isAtBottomState: ${isAtBottomState.value}") }
            .filter { it }
            .collect {
                val lastVisibleIndex = listState.getLastVisibleIndex()
                onEndList(lastVisibleIndex)
            }
    }

    LaunchedEffect(key1 = sortOptionState) {
        snapshotFlow { sortOptionState.value }
            .filterNotNull()
            .distinctUntilChanged()
            .collect {
                delay(300)
                listState.scrollToItem(0)
            }
    }

    LaunchedEffect(key1 = mostRecentDateState) {
        snapshotFlow { mostRecentDateState.value }
            .distinctUntilChanged()
            .filter { it != null }
            .collect {
                delay(300)
                listState.scrollToItem(0)
            }
    }

    rosterSizeState.intValue = roster.size
    sortOptionState.value = selectedSortOption

    val rosterRecentTime = roster.findRecentTimeInMilli()
    if (rosterRecentTime > (mostRecentDateState.value ?: 0)) {
        mostRecentDateState.value = rosterRecentTime
    }

    PageContent(
        roster = roster,
        onLike = onLike,
        onClick = onClick,
        isPaginationAvailable = isPaginationAvailable,
        pagerState = listState,
        isAtBottomState = isAtBottomState.value
    )
}

@Composable
private fun PageContent(
    roster: List<Movie>,
    onLike: (Movie) -> Unit,
    onClick: (Movie) -> Unit,
    isPaginationAvailable: Boolean,
    pagerState: LazyListState,
    isAtBottomState: Boolean,
) {
    LazyColumnScrollbar(
        state = pagerState,
        settings = ScrollbarSettings.Default.copy(
            thumbThickness = 10.dp,
            thumbShape = RoundedCornerShape(4.dp),
            thumbUnselectedColor = MaterialTheme.colorScheme.secondary,
            thumbSelectedColor = MaterialTheme.colorScheme.primary,
        ),
        indicatorContent =  { index, isThumbSelected ->
            val date = roster.getOrNull(index)?.formattedDateForDivider ?: ""
            val alpha = if (isThumbSelected) 0.9f else 0.3f
            Text(
                text = date,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = alpha),
                        shape = RoundedCornerShape(4.dp)
                    ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    ) {
        LazyColumn(state = pagerState) {
            MovieCardItems(
                roster = roster,
                onLike = onLike,
                onClick = onClick,
            )
            ProgressItem(
                roster = roster,
                isPaginationAvailable = isPaginationAvailable,
                isAtBottomState = isAtBottomState
            )
        }
    }
}

private fun LazyListScope.MovieCardItems(
    roster: List<Movie>,
    onLike: (Movie) -> Unit,
    onClick: (Movie) -> Unit
) {
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
            onLike = { onLike(item) },
            onClick = { onClick(item) }
        )
    }
}

private fun LazyListScope.ProgressItem(
    roster: List<Movie>,
    isPaginationAvailable: Boolean,
    isAtBottomState: Boolean
) {
    if (isPaginationAvailable) {
        item("Pager CircularProgressIndicator") {
            AnimatedVisibility(visible = isAtBottomState && roster.isNotEmpty()) {
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

@PreviewCombined
@Composable
private fun MoviePagerPreview() {
    PageContent(
        roster = listOf(
            Movie(
                id = 1,
                title = "Star Wars",
                description = "A long time ago in a galaxy far, far away...",
                isLiked = true,
                date = Calendar.getInstance(),
                rating = 7.66f,
                voteCount = 300
            ),
            Movie(
                id = 2,
                title = "The Lord of the Rings",
                description = "One ring",
                isLiked = false,
                date = Calendar.getInstance(),
                rating = 7.66f,
                voteCount = 300
            ),
            Movie(
                id = 3,
                title = "Shawshank Redemption",
                description = "Two imprisoned",
                isLiked = true,
                date = Calendar.getInstance(),
                rating = 7.66f,
                voteCount = 300
            ),
            Movie(
                id = 4,
                title = "The Godfather",
                description = "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
                isLiked = false,
                date = Calendar.getInstance(),
                rating = 7.66f,
                voteCount = 300
            ),
            Movie(
                id = 5,
                title = "The Dark Knight",
                description = "When the menace known as the Joker wreaks havoc and chaos on the",
                isLiked = true,
                date = Calendar.getInstance(),
                rating = 7.66f,
                voteCount = 300
            ),
            Movie(
                id = 6,
                title = "The Matrix",
                description = "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
                isLiked = false,
                date = Calendar.getInstance(),
                rating = 7.66f,
                voteCount = 300
            ),
        ),
        onLike = {},
        onClick = {},
        isPaginationAvailable = true,
        pagerState = rememberLazyListState(),
        isAtBottomState = false
    )
}