package com.velord.ui.feature.bottomnavigation.viewmodel.voyager

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import kotlin.test.Test
import kotlin.test.assertEquals

class BottomNavigationVoyagerBackTest {

    @Test
    fun `default tab root enables exit handling`() {
        val root = TestScreen.Root

        val handling = resolveVoyagerBackHandling(
            currentTab = BottomNavigationVoyagerUiState.DEFAULT.currentTab,
            startDestination = root,
            currentDestination = root,
        )
        val expected = VoyagerBackHandling(isExitEnabled = true, isTabReturnEnabled = false)

        assertEquals(expected, handling)
    }

    @Test
    fun `non default root returns to default while child delegates to Voyager`() {
        val rootHandling = resolveVoyagerBackHandling(
            currentTab = BottomNavigationItem.Demo,
            startDestination = TestScreen.Root,
            currentDestination = TestScreen.Root,
        )
        val childHandling = resolveVoyagerBackHandling(
            currentTab = BottomNavigationItem.Demo,
            startDestination = TestScreen.Root,
            currentDestination = TestScreen.Child,
        )

        assertEquals(
            VoyagerBackHandling(isExitEnabled = false, isTabReturnEnabled = true),
            rootHandling,
        )
        assertEquals(
            VoyagerBackHandling(isExitEnabled = false, isTabReturnEnabled = false),
            childHandling,
        )
    }

    private sealed class TestScreen : Screen {
        data object Root : TestScreen()
        data object Child : TestScreen()

        @Composable
        override fun Content() = Unit
    }
}
