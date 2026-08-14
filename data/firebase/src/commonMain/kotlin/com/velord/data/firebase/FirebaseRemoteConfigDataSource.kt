package com.velord.data.firebase

interface FirebaseRemoteConfigDataSource {
    suspend fun initialize(defaultLocalization: String)
    fun getLocalization(): String?
    suspend fun fetchAndActivate()
}
