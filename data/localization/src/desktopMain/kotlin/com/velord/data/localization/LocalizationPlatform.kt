package com.velord.data.localization

import com.firebasekit.core.Firebase
import com.firebasekit.core.initialize
import com.velord.infrastructure.config.GeneratedBuildConfigResolver
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Locale

internal actual object LocalizationPlatform {

    private val initializationMutex = Mutex()

    @Volatile
    private var isRemoteConfigInitialized = false

    actual suspend fun initializeRemoteConfig() {
        if (isRemoteConfigInitialized) return

        initializationMutex.withLock {
            if (isRemoteConfigInitialized) return@withLock

            val config = GeneratedBuildConfigResolver()
            val apiKey = config.getFirebaseApiKey().takeIf { it.isNotBlank() }
                ?: error("Desktop Firebase API key is missing. Set firebase.apiKey in local.properties.")
            val projectId = config.getFirebaseProjectId().takeIf { it.isNotBlank() }
                ?: error("Desktop Firebase project ID is missing. Set firebase.projectId in local.properties.")
            val appId = config.getFirebaseAppId().takeIf { it.isNotBlank() }
                ?: error("Desktop Firebase app ID is missing. Set firebase.appId in local.properties.")

            Firebase.initialize(
                apiKey = apiKey,
                projectId = projectId,
                appId = appId,
            )
            isRemoteConfigInitialized = true
        }
    }

    actual fun currentLanguageTag(): String = Locale.getDefault().toLanguageTag()
}
