package com.velord.buildlogic.plugin

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import com.velord.buildlogic.util.applyPlugin
import com.velord.buildlogic.util.versionInt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            applyPlugin("multiplatform-kotlin")
            applyPlugin("multiplatform-android-library")

            configurations.matching { it.name.startsWith("desktop") }.configureEach {
                exclude(mapOf("group" to "androidx.compose.ui"))
            }

            val targetJvmVersion = versionInt("jvmTarget")

            extensions.configure<KotlinMultiplatformExtension> {
                jvm("desktop")

                jvmToolchain(targetJvmVersion)

                val ext = (this as ExtensionAware).extensions
                ext.configure<KotlinMultiplatformAndroidLibraryExtension>(
                    "androidLibrary"
                ) {
                    compileSdk = versionInt("targetApi")
                    minSdk = versionInt("minApi")
                }
            }
        }
    }
}
