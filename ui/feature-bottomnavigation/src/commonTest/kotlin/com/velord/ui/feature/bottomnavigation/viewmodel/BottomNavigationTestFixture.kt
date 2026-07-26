package com.velord.ui.feature.bottomnavigation.viewmodel

import com.velord.ui.feature.bottomnavigation.navigation.BottomNavBackHandlingState
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavEventService
import com.velord.ui.feature.bottomnavigation.navigation.BottomNavigationItem
import com.velord.ui.feature.bottomnavigation.navigation.TabState
import com.velord.usecase.event.RequestAppExitUC
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout

internal const val TEST_TIMEOUT_MS = 1_000L

internal fun createBottomNavigationVM(
    service: BottomNavEventService = BottomNavEventService(),
    requestAppExitUC: RequestAppExitUC = RequestAppExitUC {},
): BottomNavigationVM = BottomNavigationVM(
    bottomNavEventService = service,
    requestAppExitUC = requestAppExitUC,
)

internal fun createBottomNavigationUiState(
    currentTab: BottomNavigationItem,
    isAtRoot: Boolean,
    isGranted: Boolean,
) = BottomNavigationUiState(
    tabState = TabState(previous = currentTab, current = currentTab),
    backHandlingState = BottomNavBackHandlingState(
        isAtStartGraphDestination = isAtRoot,
        isGrantedToProceed = isGranted,
    ),
)

internal suspend fun BottomNavigationVM.awaitUiState(
    predicate: (BottomNavigationUiState) -> Boolean
): BottomNavigationUiState = withTimeout(TEST_TIMEOUT_MS) {
    uiStateFlow.first(predicate)
}

internal suspend fun BottomNavigationVM.performAndAwaitTabEvent(
    action: BottomNavigationUiAction
): TabState = coroutineScope {
    val event = async(start = CoroutineStart.UNDISPATCHED) {
        withTimeout(TEST_TIMEOUT_MS) {
            onTabClickEvent.first()
        }
    }

    onAction(action)
    event.await()
}
