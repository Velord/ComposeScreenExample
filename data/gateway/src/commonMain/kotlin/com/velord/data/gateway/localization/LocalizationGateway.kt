package com.velord.data.gateway.localization

import com.velord.data.firebase.FirebaseRemoteConfigDataSource
import org.koin.core.annotation.Single

@Single
class LocalizationGateway(
    private val remoteConfig: FirebaseRemoteConfigDataSource,
) {

    suspend fun initialize(defaultLocalization: String): String? = runCatching {
        remoteConfig.initialize(defaultLocalization)
        remoteConfig.getLocalization()
    }.getOrNull()

    suspend fun fetchAndActivate() {
        runCatching {
            remoteConfig.fetchAndActivate()
        }
    }
}
