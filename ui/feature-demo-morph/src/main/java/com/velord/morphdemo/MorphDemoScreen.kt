package com.velord.morphdemo

import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.pillStar
import androidx.graphics.shapes.star
import com.velord.uicore.compose.animation.interpolator.toEasing
import com.velord.uicore.compose.path.toComposePath
import com.velord.uicore.compose.polygon.heart
import com.velord.uicore.compose.shape.MorphShape

@Composable
fun MorphDemoScreen() {
    Content()
}

@Composable
private fun Content() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        TriangleToStar()
        PillToPolygon()
        Heart()
        Spacer(modifier = Modifier.size(200.dp))
    }
}

@Composable
private fun ColumnScope.TriangleToStar() {
    val polygon = remember {
        RoundedPolygon(3)
    }
    val star = remember {
        RoundedPolygon.star(5, 0.7f)
    }
    val morph = remember {
        Morph(polygon, star)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressedState = interactionSource.collectIsPressedAsState()
    val animatedProgress = animateFloatAsState(
        targetValue = if (isPressedState.value) 1f else 0f,
        label = "TriangleToStar progress",
        animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)
    )

    Title("Triangle to Star morphing\nOn press interaction")
    Box(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(200.dp)
            .clip(MorphShape(morph, animatedProgress.value))
            .background(MaterialTheme.colorScheme.primary)
            .size(200.dp)
            .clickable(interactionSource = interactionSource, indication = null) {}
    ) {
        Text(
            text = "Press me",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.inversePrimary
        )
    }
}

@Composable
private fun ColumnScope.PillToPolygon() {
    val polygon = remember {
        RoundedPolygon(14)
    }
    val star = remember {
        RoundedPolygon.pillStar(1f, 0.36f)
    }
    val morph = remember {
        Morph(star, polygon)
    }
    val animatedProgress = rememberInfiniteTransition(label = "StarToTriangle").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        label = "StarToTriangle progress",
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Title("Pill to Polygon morphing\nInfinite transition restartable")
    Box(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(200.dp)
            .clip(
                MorphShape(
                    morph = morph,
                    progress = animatedProgress.value,
                    rotationZ = animatedProgress.value * 360f
                )
            )
            .background(MaterialTheme.colorScheme.secondary)
            .size(200.dp)
    ) {
        Text(
            text = "Watch me",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.inversePrimary
        )
    }
}



@Composable
private fun ColumnScope.Heart() {
    val polygon = remember {
        RoundedPolygon.heart()
    }
    val animatedProgress = rememberInfiniteTransition(label = "Heart").animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        label = "Heart progress",
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                easing = AnticipateOvershootInterpolator().toEasing()
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Title("Heart beating\nInfinite transition revert")
    Box(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(200.dp)
            .scale(animatedProgress.value)
            .drawWithCache {
                onDrawBehind {
                    val path = polygon.cubics.toComposePath()
                    scale(size.width * 0.5f, size.width * 0.5f) {
                        translate(size.width * 0.5f, size.height * 0.5f) {
                            drawPath(path, Color.Red)
                        }
                    }
                }
            }
    ) {
        Text(
            text = "Beating",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.inversePrimary
        )
    }
}

@Composable
private fun Title(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp),
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Preview
@Composable
private fun Preview() {
    Content()
}