package com.velord.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("velord.android.library")
        pluginManager.apply("velord.android.compose")
        pluginManager.apply("velord.android.viewbinding")

        addBundle("implementation", "kotlin-module")
        addBundle("implementation", "androidx-module")
    }
}
