package com.velord.data.localization

import com.firebasekit.core.Firebase
import com.firebasekit.core.initialize

private const val DEFAULT_PROJECT_ID = "true-artwork-239920"
private const val DEFAULT_APP_ID = "1:1078749180002:android:1f830450d6ec23b2e2613b"

internal actual suspend fun initializeRemoteConfigPlatform() {
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
        ) ?: DEFAULT_PROJECT_ID,
        appId = firebaseSetting(
            systemProperty = "firebase.appId",
            environmentVariable = "FIREBASE_APP_ID",
        ) ?: DEFAULT_APP_ID,
    )
}

private fun firebaseSetting(
    systemProperty: String,
    environmentVariable: String,
): String? = System
    .getProperty(systemProperty)
    ?.takeIf(String::isNotBlank)
    ?: System.getenv(environmentVariable)?.takeIf(String::isNotBlank)
