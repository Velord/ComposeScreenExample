package com.velord.feature.movie.component

import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.material.icons.rounded.ImageSearch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velord.model.movie.Movie
import com.velord.uicore.compose.animation.interpolator.toEasing
import com.velord.uicore.compose.preview.PreviewCombined
import kotlinx.coroutines.flow.filter
import java.util.Calendar

@Composable
internal fun MovieCard(
    movie: Movie,
    modifier: Modifier = Modifier,
    onLike: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.sizeIn(maxWidth = 64.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    imageVector = Icons.Rounded.ImageSearch,
                    contentDescription = "Movie Image",
                    modifier = Modifier.defaultMinSize(64.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = movie.formattedDateForCard,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp
                )
            }

            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.align(Alignment.CenterStart),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = movie.title,
                        modifier = Modifier.padding(end = 24.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = movie.description,
                        modifier = Modifier.padding(end = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Heart(
                    isLiked = movie.isLiked,
                    onLike = onLike
                )
            }
        }
    }
}

@Composable
private fun BoxScope.Heart(
    isLiked: Boolean,
    animationDuration: Int = 800,
    animationScale: Float = 1.35f,
    onLike: () -> Unit,
) {
    val tintColor = if (isLiked) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }
    val icon = if (isLiked) {
        Icons.Filled.Favorite
    } else {
        Icons.Outlined.HeartBroken
    }

    val isEnabledState = remember {
        mutableStateOf(false)
    }
    val scaleState = remember {
        mutableFloatStateOf(1f)
    }
    val isLikedState = remember {
        mutableStateOf(isLiked)
    }
    isLikedState.value = isLiked
    LaunchedEffect(key1 = Unit) {
        snapshotFlow { isLikedState.value }
            .filter { it }
            .collect {
                if (isEnabledState.value.not()) return@collect
                scaleState.floatValue = animationScale
            }
    }

    HeartContent(
        scaleState = scaleState,
        onAnimEnd = { scaleState.floatValue = 1f },
        tintColor = tintColor,
        icon = icon,
        animationDuration = animationDuration,
        animationScale = animationScale
    ) {
        if (isEnabledState.value.not()) {
            isEnabledState.value = true
        }
        onLike()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.HeartContent(
    scaleState: State<Float>,
    onAnimEnd: () -> Unit,
    tintColor: Color,
    icon: ImageVector,
    animationDuration: Int,
    animationScale: Float,
    onClick: () -> Unit,
) {
    val animatedProgress = animateFloatAsState(
        targetValue = scaleState.value,
        label = "HeartContent progress",
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = AnticipateOvershootInterpolator().toEasing()
        ),
    )
    LaunchedEffect(key1 = animatedProgress) {
        snapshotFlow { animatedProgress.value }
            .filter { it == animationScale }
            .collect { onAnimEnd() }
    }

    Box(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .size(46.dp)
            .padding(end = 8.dp)
            .scale(animatedProgress.value)
            .combinedClickable(
                onClick = { onClick() },
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Movie like",
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
            ,
            tint = tintColor,
        )
    }
}

@PreviewCombined
@Composable
private fun PreviewCard() {
    MovieCard(
        modifier = Modifier,
        movie = Movie(
            id = 1,
            title = "Title sdsdfdssdfsd ddd",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            isLiked = true,
            date = Calendar.getInstance().apply {
                set(Calendar.YEAR, 2021)
                set(Calendar.MONTH, 1)
                set(Calendar.DAY_OF_MONTH, 1)
            }
        ),
        onLike = {}
    )
}