package com.velord.data.localization

import com.firebasekit.core.Firebase
import com.firebasekit.remoteconfig.remoteConfig
import org.koin.core.annotation.Single

private const val LOCALIZATION_PARAMETER = "localization"

@Single
class LocalizationRemoteDataSource {

    suspend fun fetchLocalization(): String? {
        initializeRemoteConfigPlatform()
        Firebase.remoteConfig.fetchAndActivate()
        return Firebase.remoteConfig
            .getString(LOCALIZATION_PARAMETER)
            ?.takeIf(String::isNotBlank)
    }
}
