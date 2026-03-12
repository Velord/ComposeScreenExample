package com.velord.dialogDemo.component

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

object DialogDefaults {
    val animations: DialogAnimations = DialogAnimations.Default
    val modifiers: DialogModifiers = DialogModifiers.Default
    val colors: @Composable () -> DialogColors = { DialogColors.Default() }
    val textStyles: @Composable () -> DialogTextStyles = { DialogTextStyles.Default() }
}

data class DialogAnimations(
    val mainBox: DialogPredefinedAnimation,
    val background: DialogPredefinedAnimation
) {
    companion object {
        val Default = DialogAnimations(
            mainBox = DialogPredefinedAnimation.Default(),
            background = DialogPredefinedAnimation.Fade.Default
        )
    }
}

sealed class DialogPredefinedAnimation(
    open val enter: EnterTransition,
    open val exit: ExitTransition
) {
    class Default(
        override val enter: EnterTransition = fadeIn() + expandIn(),
        override val exit: ExitTransition = shrinkOut() + fadeOut(),
    ) : DialogPredefinedAnimation(enter = enter, exit = exit)

    data class Fade(
        val initialAlpha: Float,
        val durationMillis: Int,
        override val enter: EnterTransition = fadeIn(
            initialAlpha = initialAlpha,
            animationSpec = tween(durationMillis = durationMillis)
        ),
        override val exit: ExitTransition = fadeOut(
            targetAlpha = 0f,
            animationSpec = tween(durationMillis = durationMillis)
        )
    ) : DialogPredefinedAnimation(enter = enter, exit = exit) {

        companion object {
            val Default = Fade(
                initialAlpha = 0.3f,
                durationMillis = 300
            )
        }
    }

    data class None(
        override val enter: EnterTransition = EnterTransition.None,
        override val exit: ExitTransition = ExitTransition.None
    ) : DialogPredefinedAnimation(enter = enter, exit = exit) {

        companion object {
            val Default = None(
                enter = EnterTransition.None,
                exit = ExitTransition.None
            )
        }
    }

    data class FadeAndSlide(
        val initialAlpha: Float,
        val durationMillis: Int,
        override val enter: EnterTransition = fadeIn(
            initialAlpha = initialAlpha,
            animationSpec = tween(durationMillis = durationMillis)
        ) + slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = tween(durationMillis = durationMillis)
        ),
        override val exit: ExitTransition = fadeOut(
            targetAlpha = 0f,
            animationSpec = tween(durationMillis = durationMillis)
        ) + slideOutVertically(
            targetOffsetY = { it / 2 },
            animationSpec = tween(durationMillis = durationMillis)
        )
    ) : DialogPredefinedAnimation(enter = enter, exit = exit) {

        companion object {
            val Default = FadeAndSlide(
                initialAlpha = 0.3f,
                durationMillis = 300
            )
        }
    }
}

data class DialogModifiers(
    val mainBox: Modifier,
    val background: Modifier,
    val buttonContainer: Modifier,
    val positiveButton: Modifier,
    val negativeButton: Modifier,
    val title: Modifier,
    val message: Modifier,
    val divider: Modifier,
) {
    companion object {
        val Default = DialogModifiers(
            mainBox = Modifier
                .pointerInput(Unit) { detectTapGestures { } }
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp)),
            background = Modifier,
            title = Modifier.padding(horizontal = 24.dp),
            message = Modifier.padding(horizontal = 24.dp),
            buttonContainer = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            positiveButton = Modifier,
            negativeButton = Modifier,
            divider = Modifier,
        )
    }
}

data class DialogActions(
    val onDismissRequest: () -> Unit,
    val onPositiveClick: () -> Unit,
    val onNegativeClick: (() -> Unit)?
) {
    companion object {

        fun OneButton(
            onDismissRequest: () -> Unit,
        ): DialogActions = DialogActions(
            onDismissRequest = onDismissRequest,
            onPositiveClick = onDismissRequest,
            onNegativeClick = null
        )

        fun TwoButton(
            onDismissRequest: () -> Unit,
        ): DialogActions = DialogActions(
            onDismissRequest = onDismissRequest,
            onPositiveClick = onDismissRequest,
            onNegativeClick = onDismissRequest
        )
    }
}

data class DialogColors(
    val background: Color,
    val mainBox: Color,
    val title: Color,
    val message: Color,
    val positiveButton: Color,
    val negativeButton: Color,
    val divider: Color,
) {
    companion object {

        @Composable
        fun Default(): DialogColors = DialogColors(
            background = Color.Black.copy(alpha = 0.5f),
            mainBox = MaterialTheme.colorScheme.surface,
            title = MaterialTheme.colorScheme.onPrimaryContainer,
            message = MaterialTheme.colorScheme.onPrimaryContainer,
            positiveButton = MaterialTheme.colorScheme.onPrimaryContainer,
            negativeButton = MaterialTheme.colorScheme.onPrimaryContainer,
            divider = MaterialTheme.colorScheme.onSurface
        )
    }
}

data class DialogTextStyles(
    val title: TextStyle,
    val message: TextStyle,
    val positiveButton: TextStyle,
    val negativeButton: TextStyle
) {
    companion object {

        @Composable
        fun Default(): DialogTextStyles = DialogTextStyles(
            title = MaterialTheme.typography.titleMedium,
            message = MaterialTheme.typography.bodyMedium,
            positiveButton = MaterialTheme.typography.labelLarge,
            negativeButton = MaterialTheme.typography.labelLarge
        )
    }
}