package com.velord.infrastructure.config

class GeneratedBuildConfigResolver : BuildConfigResolver {

    override fun getNavigationLib(): NavigationLib {
        val navigationLibName = ProjectBuildConfig.navigationLib
        return NavigationLib.valueOf(navigationLibName)
    }

    override fun getBaseUrl(): String = ProjectBuildConfig.baseUrl

    override fun getCurrentVersion(): String = ProjectBuildConfig.currentVersion

    override fun isLoggingEnabled(): Boolean = ProjectBuildConfig.isLoggingEnabled
}
