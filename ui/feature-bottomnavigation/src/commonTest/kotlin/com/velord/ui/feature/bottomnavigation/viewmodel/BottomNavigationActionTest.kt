package com.velord.ui.feature.bottomnavigation.viewmodel

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavEventService
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.navigation.TabState
import com.velord.usecase.event.RequestAppExitUC
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BottomNavigationActionTest {

    @Test
    fun `same tab click emits reselection event`() = runBlocking {
        val viewModel = createBottomNavigationVM()

        val tabState = viewModel.performAndAwaitTabEvent(
            BottomNavigationUiAction.TabClick(BottomNavigationItem.Demo)
        )

        assertEquals(TabState.DEFAULT, tabState)
        assertTrue(tabState.isSame)
    }

    @Test
    fun `another tab click emits switch event and updates state`() = runBlocking {
        val viewModel = createBottomNavigationVM()

        val tabState = viewModel.performAndAwaitTabEvent(
            BottomNavigationUiAction.TabClick(BottomNavigationItem.Camera)
        )

        assertEquals(BottomNavigationItem.Camera, tabState.current)
        assertFalse(tabState.isSame)
        assertEquals(tabState, viewModel.uiStateFlow.value.tabState)
    }

    @Test
    fun `back click returns every non default tab to default`() = runBlocking {
        val defaultTab = TabState.DEFAULT.current
        val nonDefaultTabRoster = BottomNavigationItem.entries.filter { tab -> tab != defaultTab }

        nonDefaultTabRoster.forEach { tab ->
            val service = BottomNavEventService()
            service.updateTab(TabState(previous = defaultTab, current = tab))
            val viewModel = createBottomNavigationVM(service)

            val tabState = viewModel.performAndAwaitTabEvent(
                BottomNavigationUiAction.BackClick
            )

            assertEquals(tab, tabState.previous)
            assertEquals(defaultTab, tabState.current)
            assertFalse(tabState.isSame)
        }
    }

    @Test
    fun `double back requests app exit once`() = runBlocking {
        val exitRequest = CompletableDeferred<Unit>()
        var exitRequestCount = 0
        val viewModel = createBottomNavigationVM(
            requestAppExitUC = RequestAppExitUC {
                exitRequestCount++
                exitRequest.complete(Unit)
            }
        )

        viewModel.onAction(BottomNavigationUiAction.BackDoubleClick)
        withTimeout(TEST_TIMEOUT_MS) {
            exitRequest.await()
        }

        assertEquals(1, exitRequestCount)
    }
}
