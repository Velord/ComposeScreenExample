package com.velord.data.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await

private const val LOCALIZATION_PARAMETER = "localization"

internal actual fun createFirebaseRemoteConfigDataSource(): FirebaseRemoteConfigDataSource =
    AndroidFirebaseRemoteConfigDataSource()

private class AndroidFirebaseRemoteConfigDataSource : FirebaseRemoteConfigDataSource {

    private val remoteConfig: FirebaseRemoteConfig
        get() = FirebaseRemoteConfig.getInstance()

    override suspend fun initialize(defaultLocalization: String) {
        remoteConfig.setDefaultsAsync(
            mapOf(LOCALIZATION_PARAMETER to defaultLocalization),
        ).await()
        remoteConfig.ensureInitialized().await()
    }

    override fun getLocalization(): String? = remoteConfig
        .getValue(LOCALIZATION_PARAMETER)
        .asString()
        .takeIf(String::isNotBlank)

    override suspend fun fetchAndActivate() {
        remoteConfig.fetchAndActivate().await()
    }
}
