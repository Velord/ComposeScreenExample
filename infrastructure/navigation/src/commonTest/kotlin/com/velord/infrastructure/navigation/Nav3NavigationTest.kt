package com.velord.infrastructure.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.navigation3.runtime.NavBackStack
import com.velord.infrastructure.navigation.compose.nav3.GraphNav3
import com.velord.infrastructure.navigation.compose.nav3.navigator.BackStackNavigator
import com.velord.infrastructure.navigation.compose.nav3.navigator.NavigationState
import com.velord.infrastructure.navigation.compose.nav3.navigator.SupremeNavigatorNav3
import com.velord.infrastructure.navigation.creation.popOuterBackStack
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class Nav3NavigationTest {

    @Test
    fun `navigation state starts with one stack for every top level route`() {
        val state = createNavigationState()

        assertEquals(DEMO_ROUTE, state.startRoute)
        assertEquals(DEMO_ROUTE, state.topLevelRoute)
        assertEquals(TOP_LEVEL_ROUTE_ROSTER.toSet(), state.backStacks.keys)
        state.backStacks.forEach { (route, backStack) ->
            assertEquals(listOf(route), backStack)
        }
    }

    @Test
    fun `navigation inside active tab preserves other tab stacks`() {
        val state = createNavigationState()
        val navigator = BackStackNavigator(state)

        navigator.navigate(MOVIE_ROUTE)
        navigator.navigate(CAMERA_ROUTE)

        assertEquals(
            listOf<GraphNav3>(DEMO_ROUTE, MOVIE_ROUTE),
            state.backStacks.getValue(DEMO_ROUTE),
        )
        assertEquals(CAMERA_ROUTE, state.topLevelRoute)
        assertEquals(listOf<GraphNav3>(CAMERA_ROUTE), state.backStacks.getValue(CAMERA_ROUTE))
    }

    @Test
    fun `reselecting active tab pops only that tab to root`() {
        val state = createNavigationState()
        val navigator = BackStackNavigator(state)
        state.backStacks.getValue(DEMO_ROUTE).add(MOVIE_ROUTE)
        state.backStacks.getValue(CAMERA_ROUTE).add(GraphNav3.Main.SettingDestinationNav3)

        navigator.popToRoot()

        assertEquals(listOf<GraphNav3>(DEMO_ROUTE), state.backStacks.getValue(DEMO_ROUTE))
        assertEquals(
            listOf(CAMERA_ROUTE, GraphNav3.Main.SettingDestinationNav3),
            state.backStacks.getValue(CAMERA_ROUTE),
        )
    }

    @Test
    fun `back removes child destination from active stack`() {
        val state = createNavigationState()
        val navigator = BackStackNavigator(state)
        val activeStack = state.backStacks.getValue(DEMO_ROUTE)
        activeStack.add(MOVIE_ROUTE)

        navigator.goBack()

        assertEquals(listOf<GraphNav3>(DEMO_ROUTE), activeStack)
    }

    @Test
    fun `back from non default tab root returns to default tab`() {
        val state = createNavigationState()
        val navigator = BackStackNavigator(state)
        state.topLevelRoute = CAMERA_ROUTE

        navigator.goBack()

        assertEquals(DEMO_ROUTE, state.topLevelRoute)
        assertEquals(
            expected = listOf<GraphNav3>(CAMERA_ROUTE),
            actual = state.backStacks.getValue(CAMERA_ROUTE)
        )
    }

    @Test
    fun `back from default tab root keeps its root entry`() {
        val state = createNavigationState()
        val navigator = BackStackNavigator(state)

        navigator.goBack()

        assertEquals(DEMO_ROUTE, state.topLevelRoute)
        assertEquals(
            expected = listOf<GraphNav3>(DEMO_ROUTE),
            actual = state.backStacks.getValue(DEMO_ROUTE)
        )
    }

    @Test
    fun `camera setting navigation uses outer stack`() {
        val outerStack = mutableListOf<GraphNav3>(GraphNav3.Main.BottomNavigationDestinationNav3)
        val navigator = SupremeNavigatorNav3(
            outerStack,
            BackStackNavigator(createNavigationState()),
        )

        navigator.goToSettingFromCameraRecording()

        assertEquals(
            expected = listOf<GraphNav3>(
                GraphNav3.Main.BottomNavigationDestinationNav3,
                GraphNav3.Main.SettingDestinationNav3,
            ),
            actual = outerStack,
        )

        popOuterBackStack(outerStack)
        assertEquals(
            expected = listOf<GraphNav3>(GraphNav3.Main.BottomNavigationDestinationNav3),
            actual = outerStack
        )

        popOuterBackStack(outerStack)
        assertEquals(
            expected = listOf<GraphNav3>(GraphNav3.Main.BottomNavigationDestinationNav3),
            actual = outerStack
        )
    }

    // TODO: delete or find ::toGraphNav3
//    @Test
//    fun `every demo event maps to a unique Nav3 route`() {
//        val routeRoster = DemoNavigationEvent.entries.map(DemoNavigationEvent::toGraphNav3)
//
//        assertEquals(DemoNavigationEvent.entries.size, routeRoster.size)
//        assertEquals(routeRoster.size, routeRoster.toSet().size)
//        assertTrue(routeRoster.all { route -> route is GraphNav3.BottomTab.Demo })
//    }

    @Test
    fun `Nav3 keys serialize and restore without reflection`() {
        val route: GraphNav3 = MOVIE_ROUTE
        val serializedRoute = Json.encodeToString(GraphNav3.serializer(), route)
        val restoredRoute = Json.decodeFromString(GraphNav3.serializer(), serializedRoute)

        assertEquals(route, restoredRoute)
        assertFalse(serializedRoute.isBlank())
    }

    @Test
    fun `outer and tab back stacks serialize and restore`() {
        val backStackSerializer = serializer<NavBackStack<GraphNav3>>()
        val outerStack = NavBackStack(
            GraphNav3.Main.BottomNavigationDestinationNav3,
            SETTING_ROUTE
        )
        val demoStack = NavBackStack<GraphNav3>(DEMO_ROUTE, MOVIE_ROUTE)

        val restoredOuterStack = Json.decodeFromString(
            backStackSerializer,
            Json.encodeToString(backStackSerializer, outerStack),
        )
        val restoredDemoStack = Json.decodeFromString(
            backStackSerializer,
            Json.encodeToString(backStackSerializer, demoStack),
        )

        assertEquals(outerStack.toList(), restoredOuterStack.toList())
        assertEquals(demoStack.toList(), restoredDemoStack.toList())
    }

    private fun createNavigationState(): NavigationState = NavigationState(
        startRoute = DEMO_ROUTE,
        topLevelRoute = mutableStateOf(DEMO_ROUTE),
        backStacks = TOP_LEVEL_ROUTE_ROSTER.associateWith { route -> NavBackStack(route) },
    )

    private companion object {
        val CAMERA_ROUTE = GraphNav3.BottomTab.CameraRecording.CameraRecordingDestinationNav3
        val DEMO_ROUTE = GraphNav3.BottomTab.Demo.DemoDestinationNav3
        val SETTING_ROUTE = GraphNav3.BottomTab.SettingDestinationNav3
        val MOVIE_ROUTE = GraphNav3.BottomTab.Demo.MovieDestinationNav3
        val TOP_LEVEL_ROUTE_ROSTER = listOf(CAMERA_ROUTE, DEMO_ROUTE, SETTING_ROUTE)
    }
}
