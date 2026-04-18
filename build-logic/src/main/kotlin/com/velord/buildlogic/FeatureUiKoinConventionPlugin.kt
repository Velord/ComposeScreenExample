package com.velord.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureUiKoinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        applyPlugin("convention-feature-ui")
        applyPlugin("convention-koin")
    }
}
