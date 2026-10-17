package com.velord.ui.feature.demo.horizon

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

internal const val RGB_CHANNEL_MAX = 0xFF

internal fun parseRgbHex(value: String): Color? {
    val rgb = value.trim().removePrefix("#")
    if (rgb.length != 6 || rgb.any { it !in '0'..'9' && it !in 'a'..'f' && it !in 'A'..'F' }) {
        return null
    }
    val number = rgb.toIntOrNull(16) ?: return null
    val redByte = (number shr 16) and RGB_CHANNEL_MAX
    val greenByte = (number shr 8) and RGB_CHANNEL_MAX
    val blueByte = number and RGB_CHANNEL_MAX
    val maxChannel = RGB_CHANNEL_MAX.toFloat()
    return Color(
        red = redByte / maxChannel,
        green = greenByte / maxChannel,
        blue = blueByte / maxChannel,
        alpha = 1f,
    )
}

internal fun Color.toRgbHex(): String {
    fun channel(value: Float): String {
        val scaledChannel = value * RGB_CHANNEL_MAX
        val roundedChannel = scaledChannel.roundToInt()
        val boundedChannel = roundedChannel.coerceIn(0, RGB_CHANNEL_MAX)
        return boundedChannel.toString(16).uppercase().padStart(2, '0')
    }

    return "#${channel(red)}${channel(green)}${channel(blue)}"
}
