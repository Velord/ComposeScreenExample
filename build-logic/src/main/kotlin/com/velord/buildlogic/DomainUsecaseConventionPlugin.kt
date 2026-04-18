package com.velord.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class DomainUsecaseConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        applyPlugin("convention-android-library")

        implementationProject(":model")
        implementationBundle("kotlin-module")
    }
}
