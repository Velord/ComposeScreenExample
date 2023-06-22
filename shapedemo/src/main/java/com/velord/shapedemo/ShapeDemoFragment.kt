package com.velord.shapedemo

import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withSave
import androidx.fragment.app.Fragment
import com.velord.uicore.utils.setContentWithTheme

class ShapeDemoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        ShapeDemoScreen()
    }
}

@Composable
private fun ShapeDemoScreen() {
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
//        TicketShapeDemo()
//        PervasiveArcFromBottomShapeDemo()

        Column(Modifier) {
            repeat(5) {
                Text(
                    text = "Hello World",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .shimmering(
                            gradientColors = listOf(
                                Color.Red,
                                Color.Green,
                                Color.Blue,
                                Color.Yellow,
                                Color.Magenta,
                                Color.Cyan,
                                Color.Gray,
                                Color.Black,
                                Color.White,
                            ),
                            colorsPosition = { animatedValue ->
                                listOf(
                                    0f,
                                    animatedValue * 0.1f,
                                    animatedValue * 0.2f,
                                    animatedValue * 0.3f,
                                    animatedValue * 0.7f,
                                    animatedValue * 0.8f,
                                    animatedValue * 0.9f,
                                    animatedValue,
                                    1f
                                )
                            }
                        )
                )
            }
        }
    }
}

fun Modifier.shimmering(
    duration: Int = 1000,
    easing: Easing = LinearEasing,
    repeatMode: RepeatMode = RepeatMode.Restart,
    startColor: Color = Color.Transparent,
    centerColor: Color = Color.White,
    endColor: Color = Color.Transparent,
    // If you want to use more than 3 colors, you can use this parameter
    gradientColors: List<Color> = listOf(
        startColor,
        centerColor,
        endColor,
    ),
    // If you want to use more than 3 colors, you must declare the position of each color
    colorsPosition: (animatedValue: Float) -> List<Float> = { listOf(0f, it, 1f) }
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "")
    val animatedValueState = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = easing),
            repeatMode = repeatMode
        ),
        label = "ShimmerEffect animated value"
    )
    this.drawBehindShimmeringCanvas(
        animatedValue = animatedValueState.value,
        startColor = startColor,
        centerColor = centerColor,
        endColor = endColor,
        gradientColors = gradientColors,
        colorsPosition = colorsPosition(animatedValueState.value)
    )
}

fun Modifier.drawBehindShimmeringCanvas(
    animatedValue: Float,
    startColor: Color = Color.Transparent,
    centerColor: Color = Color.White,
    endColor: Color = Color.Transparent,
    gradientColors: List<Color> = listOf(
        startColor,
        centerColor,
        endColor,
    ),
    colorsPosition: List<Float> = listOf(0f, animatedValue, 1f)
) = this.then(
    Modifier.drawBehind {
        ShimmeringCanvas(
            animatedValue = animatedValue,
            startColor = startColor,
            centerColor = centerColor,
            endColor = endColor,
            gradientColors = gradientColors,
            colorsPosition = colorsPosition
        )
    }
)

fun DrawScope.ShimmeringCanvas(
    animatedValue: Float,
    startColor: Color = Color.Transparent,
    centerColor: Color = Color.White,
    endColor: Color = Color.Transparent,
    // If you want to use more than 3 colors, you can use this parameter
    gradientColors: List<Color> = listOf(
        startColor,
        centerColor,
        endColor,
    ),
    // If you want to use more than 3 colors, you must declare the position of each color
    colorsPosition: List<Float> = listOf(0f, animatedValue, 1f)
) {
    require(gradientColors.size == colorsPosition.size) {
        "The size of gradientColors and colorsPosition must be the same"
    }
    drawIntoCanvas {
        val width = size.width
        val height = size.height
        val shader = LinearGradientShader(
            from = Offset(0f, 0f),
            to = Offset(width, height),
            colors = gradientColors,
            colorStops = colorsPosition,
            tileMode = TileMode.Clamp
        )
        val paint = Paint().asFrameworkPaint()
        paint.shader = shader

        it.nativeCanvas.withSave {
            val rect = RectF(0f, 0f, width, height)
            drawRoundRect(rect, 8f, 8f, paint)
        }
    }
}

@Preview
@Composable
private fun ShapeDemoPreview() {
    Content()
}