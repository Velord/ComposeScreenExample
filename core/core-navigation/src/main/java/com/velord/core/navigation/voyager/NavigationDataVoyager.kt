package com.velord.core.navigation.voyager

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.Flow

data class NavigationDataVoyager(
    val screen: SharedScreenVoyager,
    val useRoot: Boolean = false
)

@Composable
fun ObserveNavigation(eventFlow: Flow<NavigationDataVoyager?>) {
    val navigator = LocalNavigator.current
    val navigationDataState = eventFlow.collectAsStateWithLifecycle(initialValue = null)
    navigationDataState.value?.let {
        val screen = rememberScreen(it.screen)
        if (it.useRoot) {
            navigator?.findRootNavigatorOrCurrent()?.push(screen)
        } else {
            navigator?.push(screen)
        }
    }
}

fun Navigator?.findRootNavigatorOrCurrent(): Navigator? {
    var navigator = this
    while (navigator?.parent != null) {
        navigator = navigator.parent
    }

    return navigator
}