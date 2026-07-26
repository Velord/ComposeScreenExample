package com.velord.buildlogic.plugin.module

import com.velord.buildlogic.model.ProjectModule
import com.velord.buildlogic.util.apiLibrary
import com.velord.buildlogic.util.applyPlugin
import com.velord.buildlogic.util.implementationBundle
import com.velord.buildlogic.util.implementationProject
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
        apiLibrary("androidx-glance-appwidget")
        implementationBundle("androidx-workmanager")
        implementationBundle("compose-ui.core")
    }
}
