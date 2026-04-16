package com.velord.buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.Actions.with
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("kotlin-multiplatform").get().get().pluginId)
                apply(libs.findPlugin("android-library").get().get().pluginId)
            }

            val targetJvmVersion = version("jvmTarget")

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget()
                jvm("desktop")

                jvmToolchain(targetJvmVersion.toInt())
            }

            extensions.configure<LibraryExtension> {
                configureLibraryAndroid(this)

                sourceSets.getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
                sourceSets.getByName("main").res.srcDirs("src/androidMain/res")
            }
        }
    }
}
