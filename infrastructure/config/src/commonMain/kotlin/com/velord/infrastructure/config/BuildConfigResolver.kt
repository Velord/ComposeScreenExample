package com.velord.infrastructure.config

interface BuildConfigResolver {
    fun getNavigationLib(): NavigationLib
    fun getBaseUrl(): String
    fun isLoggingEnabled(): Boolean
    fun getFirebaseApiKey(): String
    fun getFirebaseProjectId(): String
    fun getFirebaseAppId(): String
}

