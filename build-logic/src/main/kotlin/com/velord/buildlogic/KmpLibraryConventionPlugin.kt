package com.velord.buildlogic

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("kotlin-multiplatform").get().get().pluginId)
                apply(libs.findPlugin("android-multiplatform-library").get().get().pluginId)
            }

            val targetJvmVersion = version("jvmTarget")

            extensions.configure<KotlinMultiplatformExtension> {
                jvm("desktop")

                jvmToolchain(targetJvmVersion.toInt())

                (this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryExtension>("androidLibrary") {
                    compileSdk = version("targetApi").toInt()
                    minSdk = version("minApi").toInt()
                }
            }
        }
    }
}
