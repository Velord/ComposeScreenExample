package com.velord.data.localization

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import java.util.Locale

// Kit lacks Android fetch settings, so use its native SDK to allow fresh explicit fetches.
internal actual object LocalizationPlatform {

    actual suspend fun initializeRemoteConfig() {
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0) // 0s cache for immediate updates
            .build()
        FirebaseRemoteConfig.getInstance().setConfigSettingsAsync(settings)
    }

    actual fun currentLanguageTag(): String = Locale.getDefault().toLanguageTag()
}