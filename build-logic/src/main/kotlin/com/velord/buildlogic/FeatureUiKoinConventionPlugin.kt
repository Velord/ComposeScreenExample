package com.velord.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureUiKoinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("velord.feature.ui")
        pluginManager.apply("velord.koin")
    }
}
