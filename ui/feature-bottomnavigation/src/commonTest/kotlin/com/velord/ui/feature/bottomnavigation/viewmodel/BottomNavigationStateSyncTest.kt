package com.velord.ui.feature.bottomnavigation.viewmodel

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavBackHandlingState
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavEventService
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.navigation.TabState
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BottomNavigationStateSyncTest {

    @Test
    fun `vm observes back handling changes from shared service`() = runBlocking {
        val service = BottomNavEventService()
        val viewModel = createBottomNavigationVM(service)
        val expectedState = BottomNavBackHandlingState(
            isAtStartGraphDestination = true,
            isGrantedToProceed = true,
        )

        service.updateBackHandlingState(expectedState)
        val actualState = viewModel.awaitUiState { state ->
            state.backHandlingState == expectedState
        }

        assertEquals(expectedState, actualState.backHandlingState)
    }

    @Test
    fun `neutral route identities update root state`() = runBlocking {
        val viewModel = createBottomNavigationVM()
        val startDestinationRoster = listOf("root")

        viewModel.onAction(
            BottomNavigationUiAction.UpdateBackHandling(
                startDestinationRoster = startDestinationRoster,
                currentRoute = "child",
            )
        )
        val childState = viewModel.awaitUiState { state ->
            state.backHandlingState.isAtStartGraphDestination.not()
        }

        viewModel.onAction(
            BottomNavigationUiAction.UpdateBackHandling(
                startDestinationRoster = startDestinationRoster,
                currentRoute = "root",
            )
        )
        val rootState = viewModel.awaitUiState { state ->
            state.backHandlingState.isAtStartGraphDestination
        }

        assertFalse(childState.backHandlingState.isAtStartGraphDestination)
        assertTrue(rootState.backHandlingState.isAtStartGraphDestination)
    }

    @Test
    fun `child navigation revokes graph grant until graph completes again`() = runBlocking {
        val service = BottomNavEventService()
        val settingTab = BottomNavigationItem.Setting
        service.updateTab(TabState(previous = BottomNavigationItem.Demo, current = settingTab))
        val viewModel = createBottomNavigationVM(service)
        val startDestinationRoster = listOf("root")

        viewModel.onAction(BottomNavigationUiAction.GraphCompletedHandling)
        val grantedRootState = viewModel.awaitUiState { state ->
            state.backBehavior == BottomNavigationBackBehavior.ConfirmExit
        }

        viewModel.onAction(
            BottomNavigationUiAction.UpdateBackHandling(
                startDestinationRoster = startDestinationRoster,
                currentRoute = "child",
            )
        )
        val childState = viewModel.awaitUiState { state ->
            state.backHandlingState.isAtStartGraphDestination.not()
        }

        viewModel.onAction(
            BottomNavigationUiAction.UpdateBackHandling(
                startDestinationRoster = startDestinationRoster,
                currentRoute = "root",
            )
        )
        val returnedRootState = viewModel.awaitUiState { state ->
            state.backHandlingState.isAtStartGraphDestination &&
                state.backHandlingState.isGrantedToProceed.not()
        }

        viewModel.onAction(BottomNavigationUiAction.GraphCompletedHandling)
        viewModel.awaitUiState { state ->
            state.backBehavior == BottomNavigationBackBehavior.ConfirmExit
        }
        viewModel.onAction(BottomNavigationUiAction.GraphTakeResponsibility)
        val releasedRootState = viewModel.awaitUiState { state ->
            state.backBehavior == BottomNavigationBackBehavior.DelegateToNavigator
        }

        assertTrue(grantedRootState.backHandlingState.isGrantedToProceed)
        assertFalse(childState.backHandlingState.isGrantedToProceed)
        assertEquals(
            BottomNavigationBackBehavior.DelegateToNavigator,
            returnedRootState.backBehavior,
        )
        assertFalse(releasedRootState.backHandlingState.isGrantedToProceed)
    }

    @Test
    fun `view models synchronize tab and graph ownership through shared service`() = runBlocking {
        val service = BottomNavEventService()
        val firstViewModel = createBottomNavigationVM(service)
        val secondViewModel = createBottomNavigationVM(service)

        firstViewModel.onAction(
            BottomNavigationUiAction.TabClick(BottomNavigationItem.Camera)
        )
        val secondTabState = secondViewModel.awaitUiState { state ->
            state.tabState.current == BottomNavigationItem.Camera
        }

        secondViewModel.onAction(BottomNavigationUiAction.GraphCompletedHandling)
        val firstBackState = firstViewModel.awaitUiState { state ->
            state.backHandlingState.isGrantedToProceed
        }

        assertEquals(BottomNavigationItem.Camera, secondTabState.tabState.current)
        assertTrue(firstBackState.backHandlingState.isGrantedToProceed)
    }
}
