package com.velord.data.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.remoteConfig

private const val LOCALIZATION_PARAMETER = "localization"

internal class AndroidFirebaseRemoteConfigDataSource : FirebaseRemoteConfigDataSource {

    private val remoteConfig
        get() = Firebase.remoteConfig

    override suspend fun initialize(defaultLocalization: String) {
        remoteConfig.setDefaults(
            LOCALIZATION_PARAMETER to defaultLocalization,
        )
        remoteConfig.ensureInitialized()
    }

    override fun getLocalization(): String? = remoteConfig
        .getValue(LOCALIZATION_PARAMETER)
        .asString()
        .takeIf(String::isNotBlank)

    override suspend fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
    }
}
