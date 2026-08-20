package com.velord.ui.feature.bottomnavigation.viewmodel

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.navigation.TabState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BottomNavigationBackBehaviorTest {

    private val rootBehaviorWithoutGrant = mapOf(
        BottomNavigationItem.Camera to BottomNavigationBackBehavior.ReturnToDefaultTab,
        BottomNavigationItem.Demo to BottomNavigationBackBehavior.DelegateToNavigator,
        BottomNavigationItem.Setting to BottomNavigationBackBehavior.DelegateToNavigator,
    )
    private val rootBehaviorWithGrant = mapOf(
        BottomNavigationItem.Camera to BottomNavigationBackBehavior.ReturnToDefaultTab,
        BottomNavigationItem.Demo to BottomNavigationBackBehavior.ConfirmExit,
        BottomNavigationItem.Setting to BottomNavigationBackBehavior.ConfirmExit,
    )

    @Test
    fun `every tab has explicit root back behavior`() {
        val tabRoster = BottomNavigationItem.entries.toSet()

        assertEquals(tabRoster, rootBehaviorWithoutGrant.keys)
        assertEquals(tabRoster, rootBehaviorWithGrant.keys)
        tabRoster.forEach { tab ->
            val withoutGrant = createBottomNavigationUiState(
                currentTab = tab,
                isAtRoot = true,
                isGranted = false,
            )
            val withGrant = createBottomNavigationUiState(
                currentTab = tab,
                isAtRoot = true,
                isGranted = true,
            )

            assertEquals(rootBehaviorWithoutGrant.getValue(tab), withoutGrant.backBehavior)
            assertEquals(rootBehaviorWithGrant.getValue(tab), withGrant.backBehavior)
        }
    }

    @Test
    fun `every child destination delegates back independent of graph grant`() {
        BottomNavigationItem.entries.forEach { tab ->
            listOf(false, true).forEach { isGranted ->
                val uiState = createBottomNavigationUiState(
                    currentTab = tab,
                    isAtRoot = false,
                    isGranted = isGranted,
                )

                assertEquals(BottomNavigationBackBehavior.DelegateToNavigator, uiState.backBehavior)
            }
        }
    }

    @Test
    fun `default tab is explicit and selected`() {
        assertEquals(BottomNavigationItem.Demo, TabState.DEFAULT.current)
        assertTrue(TabState.DEFAULT.isSame)
    }
}
