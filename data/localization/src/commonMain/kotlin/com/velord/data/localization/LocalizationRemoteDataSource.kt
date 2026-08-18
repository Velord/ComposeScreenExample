package com.velord.data.localization

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.remoteConfig
import org.koin.core.annotation.Single

private const val LOCALIZATION_PARAMETER = "localization"

@Single
class LocalizationRemoteDataSource {

    private val remoteConfig get() = Firebase.remoteConfig

    suspend fun initialize(defaultLocalization: String) {
        remoteConfig.setDefaults(
            LOCALIZATION_PARAMETER to defaultLocalization,
        )
        remoteConfig.ensureInitialized()
    }

    fun getLocalization(): String? = remoteConfig
        .getValue(LOCALIZATION_PARAMETER)
        .asString()
        .takeIf(String::isNotBlank)

    suspend fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
    }
}
