package com.velord.infrastructure.navigation.compose.nav3.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3

@Composable
internal fun rememberNavigationState(
    startRoute: GraphNav3,
    topLevelRoutes: Set<GraphNav3>,
): NavigationState {
    val topLevelRoute = rememberSerializable(
        serializer = MutableStateSerializer(GraphNav3.serializer()),
    ) {
        mutableStateOf(startRoute)
    }

    return NavigationState(
        startRoute = startRoute,
        topLevelRoute = topLevelRoute,
        backStacks = topLevelRoutes.associateWith { key ->
            rememberNav3BackStack(key)
        },
    )
}

internal class NavigationState(
    val startRoute: GraphNav3,
    topLevelRoute: MutableState<GraphNav3>,
    val backStacks: Map<GraphNav3, NavBackStack<GraphNav3>>,
) {
    var topLevelRoute: GraphNav3 by topLevelRoute
    val displayedStackRoster: List<GraphNav3> get() = listOf(topLevelRoute)

    @Composable
    fun toEntries(
        entryProvider: (GraphNav3) -> NavEntry<GraphNav3>,
    ): SnapshotStateList<NavEntry<GraphNav3>> {
        val decoratedEntries = backStacks.mapValues { (_, stack) ->
            val decorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator<GraphNav3>(),
                rememberViewModelStoreNavEntryDecorator<GraphNav3>(),
            )
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = decorators,
                entryProvider = entryProvider,
            )
        }

        return displayedStackRoster
            .flatMap { decoratedEntries[it] ?: emptyList() }
            .toMutableStateList()
    }
}
