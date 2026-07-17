package com.velord.infrastructure.config

interface BuildConfigResolver {
    fun getNavigationLib(): NavigationLib
    fun getBaseUrl(): String
    fun getCurrentVersion(): String
    fun isLoggingEnabled(): Boolean
}
