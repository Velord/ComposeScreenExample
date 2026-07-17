package com.velord.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildConfigConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            applyPlugin("convention-kmp-library")
            applyPlugin("buildkonfig")
        }
    }
}
