package com.velord.infrastructure.navigation.compose.log

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.velord.ui.feature.bottomnavigation.navigation.TabState

internal fun logTabClick(controller: NavHostController, tab: TabState) {
    val graphStartId = controller.graph.startDestinationId
    val foundStartId = controller.graph.findStartDestination().id
    val currentDest = controller.currentDestination?.route
    val startName = controller.graph.findNode(foundStartId)?.route ?: "Unknown ID: $foundStartId"
    backStackLog.d { """
            --- ON TAB CLICK (${tab.current}) ---
            Current Location: $currentDest
            Root Graph Start ID: $graphStartId
            FindStartDestination: $startName
            -------------------------------------
        """.trimIndent() }
}
