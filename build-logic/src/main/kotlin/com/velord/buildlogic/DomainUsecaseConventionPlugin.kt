package com.velord.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class DomainUsecaseConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("velord.android.library")

        addProjectDependency("implementation", ":model")
        addBundle("implementation", "kotlin-module")
    }
}
