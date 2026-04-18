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
            applyPlugin("kotlin-multiplatform")
            applyPlugin("android-multiplatform-library")

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
