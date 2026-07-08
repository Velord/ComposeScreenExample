package com.velord.core.ui.compose.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.dp
import com.velord.model.ToastConfig
import com.velord.model.ToastDuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val TOAST_ENTER_DURATION_MILLIS = 360
private const val TOAST_EXIT_DURATION_MILLIS = 180
private const val TOAST_ENTER_DELAY_MILLIS = 30

private const val TOAST_ENTER_INITIAL_SCALE = 0.94f
private const val TOAST_EXIT_TARGET_SCALE = 0.98f

private val ToastEnterEasing = CubicBezierEasing(
    a = 0.16f,
    b = 1f,
    c = 0.3f,
    d = 1f,
)

private val ToastExitEasing = CubicBezierEasing(
    a = 0.7f,
    b = 0f,
    c = 0.84f,
    d = 0f,
)

internal data class ToastMessage(
    val id: Int,
    val text: String,
)

@Stable
class ToastHostState internal constructor(private val scope: CoroutineScope) {

    internal var message by mutableStateOf<ToastMessage?>(null)
        private set

    private var nextId = 0
    private var dismissJob: Job? = null

    fun show(
        text: String,
        duration: ToastDuration = ToastDuration.Short,
    ) {
        dismissJob?.cancel()

        message = ToastMessage(
            id = nextId++,
            text = text,
        )

        dismissJob = scope.launch {
            delay(duration.millis.milliseconds)
            dismiss()
        }
    }

    fun dismiss() {
        dismissJob?.cancel()
        message = null
    }
}

@Composable
fun rememberToastHostState(): ToastHostState {
    val scope = rememberCoroutineScope()

    return remember(scope) {
        ToastHostState(scope)
    }
}

@Composable
fun ToastHost(
    toastEventFlow: Flow<ToastConfig>,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.BottomCenter,
    content: @Composable BoxScope.() -> Unit,
) {
    val toastHostState = rememberToastHostState()

    LaunchedEffect(toastEventFlow) {
        toastEventFlow.collect { config ->
            toastHostState.show(
                text = config.message,
                duration = config.duration,
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        content()

        Box(
            modifier = Modifier.matchParentSize(),
            contentAlignment = alignment,
        ) {
            ToastContent(state = toastHostState)
        }
    }
}

@Composable
private fun ToastContent(state: ToastHostState) {
    AnimatedContent(
        targetState = state.message,
        label = "ToastContent",
        contentKey = { message -> message?.id },
        transitionSpec = {
            val enter = fadeIn(
                animationSpec = tween(
                    durationMillis = TOAST_ENTER_DURATION_MILLIS,
                    delayMillis = TOAST_ENTER_DELAY_MILLIS,
                    easing = ToastEnterEasing,
                ),
            ) + slideInVertically(
                animationSpec = tween(
                    durationMillis = TOAST_ENTER_DURATION_MILLIS,
                    easing = ToastEnterEasing,
                ),
                initialOffsetY = { height -> height / 3 },
            ) + scaleIn(
                animationSpec = tween(
                    durationMillis = TOAST_ENTER_DURATION_MILLIS,
                    easing = ToastEnterEasing,
                ),
                initialScale = TOAST_ENTER_INITIAL_SCALE,
                transformOrigin = TransformOrigin(
                    pivotFractionX = 0.5f,
                    pivotFractionY = 1f,
                ),
            )

            val exit = fadeOut(
                animationSpec = tween(
                    durationMillis = TOAST_EXIT_DURATION_MILLIS,
                    easing = ToastExitEasing,
                ),
            ) + slideOutVertically(
                animationSpec = tween(
                    durationMillis = TOAST_EXIT_DURATION_MILLIS,
                    easing = ToastExitEasing,
                ),
                targetOffsetY = { height -> height / 4 },
            ) + scaleOut(
                animationSpec = tween(
                    durationMillis = TOAST_EXIT_DURATION_MILLIS,
                    easing = ToastExitEasing,
                ),
                targetScale = TOAST_EXIT_TARGET_SCALE,
                transformOrigin = TransformOrigin(
                    pivotFractionX = 0.5f,
                    pivotFractionY = 1f,
                ),
            )

            enter togetherWith exit using SizeTransform(clip = false)
        },
        contentAlignment = Alignment.Center
    ) { message ->
        if (message == null) return@AnimatedContent

        ToastSurface(text = message.text)
    }
}

@Composable
private fun ToastSurface(text: String) {
    Surface(
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp,
        ),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.inverseSurface,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 10.dp,
            ),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
