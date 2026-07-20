package com.velord.buildlogic.plugin.android

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.velord.buildlogic.util.withPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidViewBindingConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        withPlugin("android-application") {
            extensions.configure<ApplicationExtension> {
                buildFeatures.viewBinding = true
            }
        }

        withPlugin("android-library") {
            extensions.configure<LibraryExtension> {
                buildFeatures.viewBinding = true
            }
        }
    }
}
