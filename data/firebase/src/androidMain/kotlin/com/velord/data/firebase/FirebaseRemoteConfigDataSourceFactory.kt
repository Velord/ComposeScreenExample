package com.velord.data.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await

private const val LOCALIZATION_PARAMETER = "localization"
private const val FETCH_EVERY_APP_START_INTERVAL_SECONDS = 0L

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
        // Firebase defaults to a 12-hour minimum fetch interval. Localization is fetched after the
        // current runtime document has been frozen, so force this launch's fetch and activate it
        // only for the next app start.
        remoteConfig.fetch(FETCH_EVERY_APP_START_INTERVAL_SECONDS).await()
        remoteConfig.activate().await()
    }
}
