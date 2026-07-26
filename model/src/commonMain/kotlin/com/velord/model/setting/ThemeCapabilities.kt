package com.velord.model.setting

import kotlinx.serialization.Serializable

@Serializable
internal data class ThemeCapabilities(
    val isSystemOsSwitchAvailable: Boolean,
    val isSystemDynamicColorAvailable: Boolean
)

internal expect val currentThemeCapabilities: ThemeCapabilities