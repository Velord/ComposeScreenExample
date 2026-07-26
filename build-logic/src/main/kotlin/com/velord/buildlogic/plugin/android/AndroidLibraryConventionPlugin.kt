package com.velord.buildlogic.plugin.android

import com.android.build.api.dsl.LibraryExtension
import com.velord.buildlogic.util.applyPlugin
import com.velord.buildlogic.util.implementationBundle
import com.velord.buildlogic.util.versionInt
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        applyPlugin("android-library")
        implementationBundle("logging-kmp")

        extensions.configure<LibraryExtension> {
            compileSdk = versionInt("targetApi")

            defaultConfig {
                minSdk = versionInt("minApi")
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_21
                targetCompatibility = JavaVersion.VERSION_21
            }

            buildTypes {
                named("release") {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            lint {
                lintConfig = rootProject.file("config/lint/lint.xml")
                abortOnError = true
                warningsAsErrors = false
                baseline = rootProject.file("config/lint/lint-baseline.xml")
            }
        }
    }
}
