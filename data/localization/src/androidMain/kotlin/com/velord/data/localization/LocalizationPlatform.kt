package com.velord.data.localization

import java.util.Locale

internal actual object LocalizationPlatform {
    actual suspend fun initializeRemoteConfig() = Unit

    actual fun currentLanguageTag(): String = Locale.getDefault().toLanguageTag()
}
