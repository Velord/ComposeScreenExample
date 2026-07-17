import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

private val baseUrl = "https://google.com"
private val buildConfigPackage = "com.velord.infrastructure.config"
private val buildConfigObject = "ProjectBuildConfig"
private val currentVersion = "120000"
private val debugNavigationLib = "Nav3"
private val releaseNavigationLib = "Destinations"

plugins {
    alias(libs.plugins.convention.buildconfig)
}

kotlin {
    android {
        namespace = "com.velord.infrastructure.config"
    }
}

buildkonfig {
    packageName = buildConfigPackage
    objectName = buildConfigObject

    defaultConfigs {
        buildConfigField(BOOLEAN, "isLoggingEnabled", "true")
        buildConfigField(STRING, "navigationLib", debugNavigationLib)
        buildConfigField(STRING, "baseUrl", baseUrl)
        buildConfigField(STRING, "currentVersion", currentVersion)
    }

    listOf("developDebug", "qaDebug", "stageDebug", "productionDebug").forEach { flavor ->
        defaultConfigs(flavor) {
            buildConfigField(BOOLEAN, "isLoggingEnabled", "true")
            buildConfigField(STRING, "navigationLib", debugNavigationLib)
            buildConfigField(STRING, "baseUrl", baseUrl)
            buildConfigField(STRING, "currentVersion", currentVersion)
        }
    }

    listOf("developRelease", "qaRelease", "stageRelease", "productionRelease").forEach { flavor ->
        defaultConfigs(flavor) {
            buildConfigField(BOOLEAN, "isLoggingEnabled", "false")
            buildConfigField(STRING, "navigationLib", releaseNavigationLib)
            buildConfigField(STRING, "baseUrl", baseUrl)
            buildConfigField(STRING, "currentVersion", currentVersion)
        }
    }
}
