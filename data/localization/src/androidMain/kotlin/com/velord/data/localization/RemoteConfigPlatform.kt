package com.velord.data.localization

/**
 * FirebaseInitProvider initializes the default Android FirebaseApp before Application.onCreate.
 * The common Remote Config client can therefore use the native default app directly.
 */
internal actual suspend fun initializeRemoteConfigPlatform() = Unit
