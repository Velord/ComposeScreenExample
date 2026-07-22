package com.velord.infrastructure.navigation.jetpack.inDevelopment

sealed interface InDevelopmentUiAction {
    data object OpenNew : InDevelopmentUiAction
}
