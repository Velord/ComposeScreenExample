package com.velord.data.firebase

internal actual fun createFirebaseRemoteConfigDataSource(): FirebaseRemoteConfigDataSource =
    DesktopFirebaseRemoteConfigDataSource()

/**
 * GitLive exposes a JVM artifact for Remote Config, but its current JVM backend delegates to
 * Firebase Android Remote Config and the GitLive Firebase Java SDK marks Remote Config as
 * non-functional on JVM. Returning no remote value keeps Desktop on the validated bundled
 * localization instead of depending on an unsupported runtime path.
 */
private class DesktopFirebaseRemoteConfigDataSource : FirebaseRemoteConfigDataSource {
    override suspend fun initialize(defaultLocalization: String) = Unit

    override fun getLocalization(): String? = null

    override suspend fun fetchAndActivate() = Unit
}
