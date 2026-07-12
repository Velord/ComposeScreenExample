package com.velord.ui.feature.movie.component

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.velord.model.movie.Movie
import kotlinx.datetime.TimeZone
import kotlin.math.roundToInt

@Composable
internal actual fun MovieScrollbar(
    roster: List<Movie>,
    state: LazyListState,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isDragged by interactionSource.collectIsDraggedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    BoxWithConstraints {
        content()
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(state),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            interactionSource = interactionSource,
        )
        if (roster.isNotEmpty()) {
            MovieDateIndicator(
                roster = roster,
                state = state,
                isSelected = isDragged || isHovered,
                availableHeightPx = constraints.maxHeight,
                modifier = Modifier.align(Alignment.TopEnd),
            )
        }
    }
}

@Composable
private fun MovieDateIndicator(
    roster: List<Movie>,
    state: LazyListState,
    isSelected: Boolean,
    availableHeightPx: Int,
    modifier: Modifier = Modifier,
) {
    val progress by remember(state, roster.size) {
        derivedStateOf { state.scrollProgress() }
    }
    val date by remember(state, roster) {
        derivedStateOf {
            val timeZone = TimeZone.currentSystemDefault()
            roster.getOrNull(state.firstVisibleItemIndex)?.formattedDateForDivider(timeZone) ?: ""
        }
    }
    var indicatorHeightPx by remember { mutableIntStateOf(0) }
    val maxOffsetPx = (availableHeightPx - indicatorHeightPx).coerceAtLeast(0)
    val alpha = if (isSelected) 0.9f else 0.3f

    Text(
        text = date,
        modifier = modifier
            .padding(end = 14.dp)
            .onSizeChanged { size -> indicatorHeightPx = size.height }
            .offsetY { (maxOffsetPx * progress).roundToInt() }
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = alpha),
                shape = RoundedCornerShape(4.dp),
            ),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
    )
}

private fun LazyListState.scrollProgress(): Float {
    val visibleItemRoster = layoutInfo.visibleItemsInfo
    val firstVisibleItem = visibleItemRoster.firstOrNull() ?: return 0f
    val itemOffsetProgress = if (firstVisibleItem.size == 0) {
        0f
    } else {
        (-firstVisibleItem.offset.toFloat() / firstVisibleItem.size).coerceIn(0f, 1f)
    }
    val scrollableItemCount = (layoutInfo.totalItemsCount - visibleItemRoster.size).coerceAtLeast(1)
    return ((firstVisibleItemIndex + itemOffsetProgress) / scrollableItemCount).coerceIn(0f, 1f)
}

private fun Modifier.offsetY(offset: () -> Int): Modifier = this.then(
    Modifier.offset { IntOffset(x = 0, y = offset()) },
)
