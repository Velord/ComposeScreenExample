package com.velord.feature.movie.component

import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.HeartBroken
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
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImage
import com.velord.model.movie.Movie
import com.velord.uicore.compose.animation.interpolator.toEasing
import com.velord.uicore.compose.preview.PreviewCombined
import kotlinx.coroutines.flow.filter
import java.util.Calendar

@Composable
internal fun MovieCard(
    movie: Movie,
    modifier: Modifier = Modifier,
    onLike: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            val (asyncImage, text) = createRefs()
            AsyncImage(
                model = movie.imageUrl,
                contentDescription = "Movie Image",
                modifier = Modifier
                    .sizeIn(maxWidth = 64.dp)
                    .constrainAs(asyncImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                ,
                contentScale = ContentScale.FillBounds
            )

            Box(
                modifier = Modifier
                    .padding(start = 72.dp)
                    .padding(vertical = 8.dp)
                    .constrainAs(text) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.align(Alignment.TopStart),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    TitleAndDescription(movie = movie)
                    DateAndRating(movie = movie)
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
private fun TitleAndDescription(movie: Movie) {
    Text(
        text = movie.title,
        modifier = Modifier.padding(end = 28.dp),
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

@Composable
private fun DateAndRating(movie: Movie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = movie.formattedDateForCard,
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp
        )

        Row(
            modifier = Modifier.padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Movie rating",
                modifier = Modifier.size(16.dp),
                tint = Color.Yellow,
            )
            Text(
                text = movie.rating.toString(),
                modifier = Modifier.padding(start = 4.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelMedium,
            )
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
            title = "Title sdsdfssdsfsd ddfd",
            description = "Lorem ipsum dolor sit amet, consectetur adipLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et discing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            isLiked = true,
            date = Calendar.getInstance().apply {
                set(Calendar.YEAR, 2021)
                set(Calendar.MONTH, 1)
                set(Calendar.DAY_OF_MONTH, 1)
            },
            rating = 4.5f,
            voteCount = 100,
        ),
        onLike = {},
        onClick = {}
    )
}