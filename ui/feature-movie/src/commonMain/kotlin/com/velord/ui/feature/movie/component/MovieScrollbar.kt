package com.velord.ui.feature.movie.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import com.velord.model.movie.Movie

@Composable
internal expect fun MovieScrollbar(
    roster: List<Movie>,
    state: LazyListState,
    content: @Composable () -> Unit,
)
