package com.velord.data.firebase

internal class DesktopFirebaseRemoteConfigDataSource : FirebaseRemoteConfigDataSource {

    override suspend fun initialize(defaultLocalization: String) = Unit

    override fun getLocalization(): String? = null

    override suspend fun fetchAndActivate() = Unit
}
