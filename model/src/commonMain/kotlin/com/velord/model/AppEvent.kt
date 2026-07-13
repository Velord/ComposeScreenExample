package com.velord.model

sealed interface AppEvent {
    data class Toast(val config: ToastConfig) : AppEvent
    data object Exit : AppEvent
}
