package com.velord.infrastructure.navigation.compose.destinations.transition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle

private const val FADE_IN_DURATION = 700

internal object PopFadeTransition : NavHostAnimatedDestinationStyle() {

    override val enterTransition: DestinationTransitionScope.() -> EnterTransition = {
        fadeIn(animationSpec = tween(FADE_IN_DURATION))
    }

    override val exitTransition: DestinationTransitionScope.() -> ExitTransition = { fadeOut() }

    override val popEnterTransition: DestinationTransitionScope.() -> EnterTransition =
        DefaultTransition.popEnterTransition

    override val popExitTransition: DestinationTransitionScope.() -> ExitTransition =
        DefaultTransition.popExitTransition
}
