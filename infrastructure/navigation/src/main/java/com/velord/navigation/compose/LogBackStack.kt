package com.velord.navigation.compose

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
import com.ramcosta.composedestinations.utils.route

@SuppressLint("RestrictedApi")
@Composable
fun LogBackStack(
    navController: NavController,
    tag: String,
) {
    LaunchedEffect(navController) {
        navController.currentBackStack.collect {
            it.print(tag = tag)
        }
    }
}

private fun Collection<NavBackStackEntry>.print(tag: String, prefix: String = "stack") {
    val stack = toMutableList().toRouteArgs().toTypedArray().contentToString()
    Logger.d(tag = "LogBackStack - $tag") { "$prefix = $stack" }
}

private fun MutableList<NavBackStackEntry>.toRouteArgs(): List<String> = map { entry ->
    val route = entry.route()
    val args = runCatching { route.argsFrom(entry) }
        .getOrNull()
        ?.takeIf { it != Unit }
        ?.let { "(args={$it})" } ?: ""
    "$route$args"
}
