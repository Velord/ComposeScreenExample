import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.velord.buildlogic.model.BuildEnvironment
import com.velord.buildlogic.model.BuildType

private val baseUrl = "https://google.com"
private val buildConfigPackage = "com.velord.infrastructure.config"
private val buildConfigObject = "ProjectBuildConfig"
private val debugNavigationLib = "Destinations"
private val releaseNavigationLib = "Nav3"

plugins {
    alias(libs.plugins.convention.build.config)
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
    }

    BuildEnvironment.entries.forEach { environment ->
        val flavor = environment.variantName(BuildType.Debug)
        defaultConfigs(flavor) {
            buildConfigField(BOOLEAN, "isLoggingEnabled", "true")
            buildConfigField(STRING, "navigationLib", debugNavigationLib)
            buildConfigField(STRING, "baseUrl", baseUrl)
        }
    }

    BuildEnvironment.entries.forEach { environment ->
        val flavor = environment.variantName(BuildType.Release)
        defaultConfigs(flavor) {
            buildConfigField(BOOLEAN, "isLoggingEnabled", "false")
            buildConfigField(STRING, "navigationLib", releaseNavigationLib)
            buildConfigField(STRING, "baseUrl", baseUrl)
        }
    }
}
