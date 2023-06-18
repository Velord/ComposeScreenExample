package com.velord.shapedemo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velord.uicore.compose.component.PervasiveArcFromBottomLayout

@Composable
internal fun PervasiveArcFromBottomShapeDemo() {
    Title(text = "Pervasive arc from bottom shape demo")

    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceTint),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val isVisibleState = remember { mutableStateOf(false) }
        Button(
            onClick = { isVisibleState.value = !isVisibleState.value },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .clip(RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        ) {
            val text = if (isVisibleState.value) "Hide" else "Show"
            Text(text = text)
        }

        PervasiveArcFromBottomLayout(
            isVisible = isVisibleState.value,
            abortAnimationOnEdge = true
        ) { currentShape, defaultShape ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .height(100.dp)
                    .fillMaxWidth()
                    .clip(currentShape)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.error,
                        shape = defaultShape
                    )
                    .background(color = MaterialTheme.colorScheme.inverseSurface),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "When edge is reached animation is aborted",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }

        PervasiveArcFromBottomLayout(
            isVisible = isVisibleState.value,
            abortAnimationOnEdge = false,
            shape = RoundedCornerShape(32.dp)
        ) { currentShape, defaultShape ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .defaultMinSize(100.dp)
                    .clip(defaultShape)
                    .clip(currentShape)
                    .background(color = MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Without aborting",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        PervasiveArcFromBottomLayout(
            isVisible = isVisibleState.value,
            abortAnimationOnEdge = false,
            shape = CircleShape,
            animationDuration = 2300
        ) { currentShape, defaultShape ->
            val textWidthState: MutableState<Dp?> = remember { mutableStateOf(null) }
            val modifierWithCalculatedSize: State<Modifier> = remember(textWidthState.value) {
                val mod = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                derivedStateOf {
                    val currentWidth = textWidthState.value
                    if (currentWidth != null) mod.size(currentWidth) else mod
                }
            }
            Box(
                modifier = modifierWithCalculatedSize.value
                    .clip(defaultShape)
                    .clip(currentShape)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                val density = LocalDensity.current
                Text(
                    text = "Smooth animation",
                    modifier = Modifier
                        .onGloballyPositioned {
                            textWidthState.value =  with(density) { it.size.width.toDp() }
                        }
                        .padding(8.dp)
                    ,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}