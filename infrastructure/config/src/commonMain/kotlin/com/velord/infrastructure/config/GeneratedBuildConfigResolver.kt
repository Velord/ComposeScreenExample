package com.velord.infrastructure.config

class GeneratedBuildConfigResolver : BuildConfigResolver {

    override fun getNavigationLib(): NavigationLib {
        val navigationLibName = ProjectBuildConfig.navigationLib
        return NavigationLib.valueOf(navigationLibName)
    }

    override fun getBaseUrl(): String = ProjectBuildConfig.baseUrl

    override fun isLoggingEnabled(): Boolean = ProjectBuildConfig.isLoggingEnabled

    override fun getFirebaseApiKey(): String = ProjectBuildConfig.firebaseApiKey

    override fun getFirebaseProjectId(): String = ProjectBuildConfig.firebaseProjectId

    override fun getFirebaseAppId(): String = ProjectBuildConfig.firebaseAppId
}

