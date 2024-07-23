package com.velord.feature.movie.component

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.velord.feature.movie.model.MoviePage
import com.velord.uicore.compose.preview.PreviewCombined

@Composable
internal fun MovieHeader(
    currentPage: Int,
    pages: List<MoviePage>,
    onClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp))
        ,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        pages.forEachIndexed { index, moviePage ->
            HeaderButton(
                page = moviePage,
                isActive = index == currentPage,
                onClick = {
                    onClick(index)
                }
            )
        }
    }
}

@Composable
private fun RowScope.HeaderButton(
    page: MoviePage,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val high = MaterialTheme.colorScheme.primaryContainer
    val low = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    val containerColor = remember {
        Animatable(high)
    }
    val brush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface,
            containerColor.value,
            containerColor.value,
            containerColor.value
        )
    )

    LaunchedEffect(isActive) {
        val colorTo = if (isActive) high else low
        containerColor.animateTo(colorTo, animationSpec = tween(700))
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(brush),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(vertical = 0.dp)
    ) {
        val tint = when (page) {
            MoviePage.All -> MaterialTheme.colorScheme.onPrimaryContainer
            MoviePage.Favorite -> MaterialTheme.colorScheme.error
        }
        Icon(
            imageVector = page.imageRes,
            contentDescription = "Header Icon",
            tint = tint
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(id = page.titleRes),
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    MovieHeader(
        currentPage = 0,
        pages = MoviePage.entries,
        onClick = {}
    )
}