package com.velord.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        applyPlugin("kotlin-plugin-compose")

        withPlugin("android-application") {
            extensions.configure<ApplicationExtension> {
                buildFeatures.compose = true
            }
        }

        withPlugin("android-library") {
            extensions.configure<LibraryExtension> {
                buildFeatures.compose = true
            }
        }

        tasks.withType(KotlinCompile::class.java).configureEach {
            compilerOptions.freeCompilerArgs.add("-Xcontext-parameters")
        }
    }
}
