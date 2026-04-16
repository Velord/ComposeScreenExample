package com.velord.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class WidgetGlanceConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("velord.android.library")
        pluginManager.apply("velord.android.compose")

        addProjectDependency("implementation", ":infrastructure:util")
        addProjectDependency("implementation", ":core:core-resource")
        addProjectDependency("implementation", ":core:core-ui")

        addBundle("implementation", "kotlin-module")
        addBundle("implementation", "androidx-glance")
        addBundle("implementation", "androidx-workmanager")
        addBundle("implementation", "compose-ui")
    }
}
