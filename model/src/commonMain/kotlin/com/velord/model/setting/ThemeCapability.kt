package com.velord.model.setting

import kotlinx.serialization.Serializable

@Serializable
internal data class ThemeCapability(
    val isSystemOsSwitchAvailable: Boolean,
    val isSystemDynamicColorAvailable: Boolean,
)

internal expect val currentThemeCapability: ThemeCapability
