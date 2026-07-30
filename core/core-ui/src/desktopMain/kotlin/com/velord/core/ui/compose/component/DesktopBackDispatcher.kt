package com.velord.core.ui.compose.component

import androidx.compose.runtime.staticCompositionLocalOf

val LocalDesktopBackDispatcher = staticCompositionLocalOf<DesktopBackDispatcher?> { null }

class DesktopBackDispatcher {
    private val handlers = mutableListOf<() -> Boolean>()

    fun register(handler: () -> Boolean) {
        handlers.add(handler)
    }

    fun unregister(handler: () -> Boolean) {
        handlers.remove(handler)
    }

    fun onBackPressed(): Boolean {
        // Find the last registered handler (top of the stack) and execute it
        for (handler in handlers.reversed()) {
            if (handler()) return true // Event consumed
        }
        return false // Event not consumed
    }
}
