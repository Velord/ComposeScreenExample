package com.velord.infrastructure.navigation.compose.nav3.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3
import kotlinx.serialization.serializer

internal class BackStackNavigator(val state: NavigationState) {

    fun navigate(route: GraphNav3) {
        if (route in state.backStacks.keys) {
            // This is a top level route, just switch to it
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {
        val currentStack = state
            .backStacks[state.topLevelRoute]
            ?: error("Stack for $state.topLevelRoute not found")
        val currentRoute = currentStack.last()

        // If we're at the base of the current route, go back to the start route stack.
        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeAt(currentStack.lastIndex)
        }
    }

    fun popToRoot() {
        val currentStack = state.backStacks[state.topLevelRoute] ?: return
        // Keep only the first element (the start route of this tab)
        while (currentStack.size > 1) {
            currentStack.removeAt(currentStack.lastIndex)
        }
    }
}

@Composable
internal fun rememberNav3BackStack(key: GraphNav3): NavBackStack<GraphNav3> = rememberSerializable(
    serializer = serializer(),
) {
    NavBackStack(key)
}
