package com.velord.infrastructure.navigation.jetpack.inDevelopment

/**
 * Actions supported by the Jetpack recursive-navigation diagnostic.
 */
sealed interface InDevelopmentUiAction {
    data object OpenNew : InDevelopmentUiAction
}
