package com.velord.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class WidgetGlanceConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        applyPlugin("convention-android-library")
        applyPlugin("convention-android-compose")

        implementationProject(ProjectModule.INFRASTRUCTURE_UTIL)
        implementationProject(ProjectModule.CORE_RESOURCE)
        implementationProject(ProjectModule.CORE_UI)

        implementationBundle("kotlin-module")
        implementationBundle("androidx-glance")
        implementationBundle("androidx-workmanager")
        implementationBundle("compose-ui")
    }
}
