package com.velord.infrastructure.navigation.compose.log

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger

internal val backStackLog = Logger.withTag("LogBackStack")

@Suppress("MagicNumber")
@SuppressLint("RestrictedApi")
@Composable
internal fun LogNavigationEvents(navController: NavHostController, label: String) {
    LaunchedEffect(navController) {
        val flow = navController.currentBackStackEntryFlow
        flow.collect { entry ->
            val stack = navController.currentBackStack.value
            val sb = StringBuilder()
            sb.append("\n--- NAV STATE CHANGE ($label) ---\n")
            sb.append("Current Dest: ${entry.destination.route}\n")
            sb.append("Stack Size: ${stack.size}\n")
            stack.forEachIndexed { index, backStackEntry ->
                sb.append("  [$index] ${backStackEntry.destination.route} \n")
                sb.append("      (ID: ${backStackEntry.id.subSequence(0, 8)}...) \n")
                sb.append("      (Lifecycle: ${backStackEntry.lifecycle.currentState})\n")
            }
            sb.append("--------------------------------\n")
            backStackLog.d { sb.toString() }
        }
    }
}
