package com.velord.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureUiConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        applyPlugin("convention-android-library")
        applyPlugin("convention-android-compose")
        applyPlugin("convention-android-viewbinding")

        implementationBundle("kotlin-module")
        implementationBundle("androidx-module")
    }
}
