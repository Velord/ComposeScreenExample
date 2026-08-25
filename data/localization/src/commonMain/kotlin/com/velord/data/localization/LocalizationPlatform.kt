package com.velord.data.localization

internal expect object LocalizationPlatform {
    suspend fun initializeRemoteConfig()
    fun currentLanguageTag(): String
}
