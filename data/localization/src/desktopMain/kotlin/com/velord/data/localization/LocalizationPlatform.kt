package com.velord.data.localization

import com.firebasekit.core.Firebase
import com.firebasekit.core.initialize
import java.util.Locale

internal actual object LocalizationPlatform {
    actual suspend fun initializeRemoteConfig() {
        Firebase.initialize(
            apiKey = firebaseSetting(
                systemProperty = "firebase.apiKey",
                environmentVariable = "FIREBASE_API_KEY",
            ) ?: error(
                "Desktop Firebase API key is missing. Set -Dfirebase.apiKey or FIREBASE_API_KEY.",
            ),
            projectId = firebaseSetting(
                systemProperty = "firebase.projectId",
                environmentVariable = "FIREBASE_PROJECT_ID",
            ) ?: error(
                "Desktop Firebase project ID is missing. Set -Dfirebase.projectId or FIREBASE_PROJECT_ID.",
            ),
            appId = firebaseSetting(
                systemProperty = "firebase.appId",
                environmentVariable = "FIREBASE_APP_ID",
            ) ?: error(
                "Desktop Firebase app ID is missing. Set -Dfirebase.appId or FIREBASE_APP_ID.",
            ),
        )
    }

    actual fun currentLanguageTag(): String = Locale.getDefault().toLanguageTag()

    private fun firebaseSetting(
        systemProperty: String,
        environmentVariable: String,
    ): String? = System
        .getProperty(systemProperty)
        ?.takeIf(String::isNotBlank)
        ?: System.getenv(environmentVariable)?.takeIf(String::isNotBlank)
}
