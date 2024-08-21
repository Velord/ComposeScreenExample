package com.velord.uicore.compose.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt

private const val SLIDING_OFFSET_CATALYST = 500

@Composable
fun Modifier.bottomBorder(thickness: Dp, color: Color): Modifier {
    val density = LocalDensity.current
    val strokeWidthPx = density.run { thickness.toPx() }
    return this then Modifier.drawBehind {
        val width = size.width
        val height = size.height

        drawLine(
            color = color,
            start = Offset(x = 0f, y = height),
            end = Offset(x = width, y = height),
            strokeWidth = strokeWidthPx
        )
    }
}

@Composable
private fun Collaps() {
    // Here we use LazyColumn that has build-in nested scroll, but we want to act like a
    // Parent for this LazyColumn and participate in its nested scroll.
    // Let's make a collapsing toolbar for LazyColumn
    val toolbarHeightState = remember {
        mutableStateOf(64.dp)
    }
    val density = LocalDensity.current
    val toolbarHeightPxState = remember {
        derivedStateOf { with(density) { toolbarHeightState.value.roundToPx().toFloat() } }
    }
    // Our offset to collapse toolbar
    val toolbarOffsetHeightPxState = remember { mutableFloatStateOf(0f) }
    // Now, let's create connection to the nested scroll system
    // And listen to the scroll happening inside child LazyColumn
    val alphaState = remember {
        derivedStateOf {
            min(1f, abs(toolbarOffsetHeightPxState.floatValue) / (toolbarHeightPxState.value / 3f))
        }
    }
    Log.d("@@@", "alphaState: ${alphaState.value}")
    Log.d("@@@", "toolbarOffsetHeightPxState: ${toolbarOffsetHeightPxState.value}")

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll
                val delta = available.y
                val newOffset = toolbarOffsetHeightPxState.floatValue + delta
                toolbarOffsetHeightPxState.floatValue = newOffset.coerceIn(-toolbarHeightPxState.value, 0f)
                // here's the catch: let's pretend we consumed 0 in any case, since we want
                // LazyColumn to scroll anyway for good UX
                // We're basically watching scroll without taking it
                return Offset.Zero
            }
        }
    }
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {}
        }
        CollapsingToolbar(
            url = "https://picsum.photos/128/128",
            name = "",
            phone = "",
            alpha = alphaState.value,
            toolbarHeightPx = toolbarHeightPxState,
            toolbarOffsetHeightPx = toolbarOffsetHeightPxState
        ) {
            toolbarHeightState.value = it
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollapsingToolbar(
    url: String,
    name: String,
    phone: String,
    alpha: Float,
    toolbarHeightPx: State<Float>,
    toolbarOffsetHeightPx: State<Float>,
    onHeightReceive: (Dp) -> Unit
) {
    Box {
        TopAppBar(
            modifier = Modifier
                .alpha(alpha)
                .offset {
                    // Start from the status bar position
                    val start = -toolbarHeightPx.value.roundToInt()
                    // Slide from up to down
                    val offset = -toolbarOffsetHeightPx.value.roundToInt()
                    // Screen is to small to show the whole toolbar with default sliding offset
                    val catalyst = SLIDING_OFFSET_CATALYST
                    val total = min(0, start + (offset + catalyst))
                    IntOffset(x = 0, y = total)
                }
                .onGloballyPositioned {
                    onHeightReceive(it.size.height.dp)
                }
                .bottomBorder(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
            ,
            title = {
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = url,
                        contentDescription = "Profile image",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = phone,
                            modifier = Modifier.padding(top = 4.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        )
    }
}