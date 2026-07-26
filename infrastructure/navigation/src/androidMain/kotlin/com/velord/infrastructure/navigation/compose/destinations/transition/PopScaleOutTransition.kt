package com.velord.infrastructure.navigation.compose.destinations.transition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle
import com.velord.infrastructure.navigation.compose.transition.popScaleOutTransition

internal object PopScaleOutTransition : NavHostAnimatedDestinationStyle() {

    override val enterTransition: DestinationTransitionScope.() -> EnterTransition =
        DefaultTransition.enterTransition

    override val exitTransition: DestinationTransitionScope.() -> ExitTransition =
        DefaultTransition.exitTransition

    override val popEnterTransition: DestinationTransitionScope.() -> EnterTransition =
        DefaultTransition.popEnterTransition

    override val popExitTransition: DestinationTransitionScope.() -> ExitTransition = {
        popScaleOutTransition
    }
}
