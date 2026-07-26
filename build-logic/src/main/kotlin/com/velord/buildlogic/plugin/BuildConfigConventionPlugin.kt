package com.velord.buildlogic.plugin

import com.velord.buildlogic.util.BuildConfigFlavorResolver
import com.velord.buildlogic.util.applyPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

private const val BUILD_CONFIG_FLAVOR_PROPERTY = "buildkonfig.flavor"

class BuildConfigConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            applyPlugin("convention-multiplatform-library")
            configureBuildConfigFlavor()
            applyPlugin("buildkonfig")
        }
    }

    // BuildKonfig generates one global object per invocation, not one per Android variant.
    // Full variant tasks can select one matching flavor. App aggregate tasks are rejected because
    // one generated object cannot represent multiple variants in the same invocation.
    private fun Project.configureBuildConfigFlavor() {
        val isPresent = providers.gradleProperty(BUILD_CONFIG_FLAVOR_PROPERTY).isPresent
        if (isPresent || hasProperty(BUILD_CONFIG_FLAVOR_PROPERTY)) return

        val flavor = BuildConfigFlavorResolver.resolve(gradle.startParameter.taskNames)
        flavor?.let {
            extensions.extraProperties.set(BUILD_CONFIG_FLAVOR_PROPERTY, flavor)
        }
    }
}
