package com.velord.data.gateway.localization

import com.velord.core.resource.LocalizationRuntime
import com.velord.core.resource.readBundledLocalizationJson
import com.velord.data.firebase.FirebaseRemoteConfigDataSource
import com.velord.data.gateway.setting.LanguagePreferenceGateway
import org.koin.core.annotation.Single

@Single
class LocalizationGateway(
    private val remoteConfig: FirebaseRemoteConfigDataSource,
    private val languagePreferenceGateway: LanguagePreferenceGateway,
) {

    suspend fun initialize() {
        val bundledLocalization = readBundledLocalizationJson()
        val remoteLocalization = runCatching {
            remoteConfig.initialize(bundledLocalization)
            remoteConfig.getLocalization()
        }.getOrNull()

        LocalizationRuntime.initialize(
            bundledJson = bundledLocalization,
            remoteJson = remoteLocalization,
            preference = languagePreferenceGateway.get(),
        )
    }

    suspend fun fetchAndActivate() {
        runCatching {
            remoteConfig.fetchAndActivate()
        }
    }
}
