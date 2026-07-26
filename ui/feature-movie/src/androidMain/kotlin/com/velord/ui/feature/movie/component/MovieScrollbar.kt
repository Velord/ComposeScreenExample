package com.velord.ui.feature.movie.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velord.model.movie.Movie
import kotlinx.datetime.TimeZone
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSettings

@Composable
internal actual fun MovieScrollbar(
    roster: List<Movie>,
    state: LazyListState,
    content: @Composable () -> Unit,
) {
    LazyColumnScrollbar(
        state = state,
        settings = ScrollbarSettings.Default.copy(
            thumbThickness = 10.dp,
            thumbShape = RoundedCornerShape(4.dp),
            thumbUnselectedColor = MaterialTheme.colorScheme.secondary,
            thumbSelectedColor = MaterialTheme.colorScheme.primary,
        ),
        indicatorContent = { index, isThumbSelected ->
            val timeZone = TimeZone.currentSystemDefault()
            val date = roster.getOrNull(index)?.formattedDateForDivider(timeZone) ?: ""
            val alpha = if (isThumbSelected) 0.9f else 0.3f
            Text(
                text = date,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = alpha),
                        shape = RoundedCornerShape(4.dp),
                    ),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        },
        content = content,
    )
}
