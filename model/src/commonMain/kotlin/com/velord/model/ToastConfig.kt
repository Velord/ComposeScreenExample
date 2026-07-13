package com.velord.model

private const val SHORT_TOAST_DURATION_MILLIS = 2_000L
private const val LONG_TOAST_DURATION_MILLIS = 3_500L

enum class ToastDuration(val millis: Long) {
    Short(SHORT_TOAST_DURATION_MILLIS),
    Long(LONG_TOAST_DURATION_MILLIS),
}

data class ToastConfig(
    val message: String,
    val duration: ToastDuration,
)
