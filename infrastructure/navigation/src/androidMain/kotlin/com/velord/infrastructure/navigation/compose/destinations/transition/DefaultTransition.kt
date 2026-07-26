package com.velord.infrastructure.navigation.compose.destinations.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle

private const val TRANSITION_DURATION_MS = 700

internal typealias DestinationTransitionScope = AnimatedContentTransitionScope<NavBackStackEntry>

internal object DefaultTransition : NavHostAnimatedDestinationStyle() {

    override val enterTransition: DestinationTransitionScope.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { 1000 },
            animationSpec = tween(TRANSITION_DURATION_MS)
        )
    }

    override val exitTransition: DestinationTransitionScope.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { -1000 },
            animationSpec = tween(TRANSITION_DURATION_MS)
        )
    }

    override val popEnterTransition: DestinationTransitionScope.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { -1000 },
            animationSpec = tween(TRANSITION_DURATION_MS)
        )
    }

    override val popExitTransition: DestinationTransitionScope.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { 1000 },
            animationSpec = tween(TRANSITION_DURATION_MS)
        )
    }
}
