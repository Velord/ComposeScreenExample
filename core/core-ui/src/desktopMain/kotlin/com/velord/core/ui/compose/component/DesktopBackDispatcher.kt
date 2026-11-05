package com.velord.core.ui.compose.component

import androidx.compose.runtime.staticCompositionLocalOf

val LocalDesktopBackDispatcher = staticCompositionLocalOf<DesktopBackDispatcher?> { null }

@JvmInline
value class DesktopBackHandlerWrapper(val f: () -> Boolean)

class DesktopBackDispatcher {

    private val handlers = mutableListOf<DesktopBackHandlerWrapper>()

    fun register(handler: DesktopBackHandlerWrapper) {
        handlers.add(handler)
    }

    fun unregister(handler: DesktopBackHandlerWrapper) {
        handlers.remove(handler)
    }

    fun onBackPressed(): Boolean {
        // Find the last registered handler (top of the stack) and execute it
        for (handler in handlers.reversed()) {
            if (handler.f()) return true // Event consumed
        }

        return false // Event not consumed
    }
}
