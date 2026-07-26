package com.velord.buildlogic.plugin.android

import com.android.build.api.dsl.ApplicationExtension
import com.velord.buildlogic.util.applyPlugin
import com.velord.buildlogic.util.implementationBundle
import com.velord.buildlogic.util.versionInt
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        applyPlugin("android-application")
        implementationBundle("logging-kmp")

        extensions.configure<ApplicationExtension> {
            compileSdk = versionInt("targetApi")

            defaultConfig {
                minSdk = versionInt("minApi")
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_21
                targetCompatibility = JavaVersion.VERSION_21
            }

            lint {
                lintConfig = rootProject.file("config/lint/lint.xml")
                abortOnError = true
                warningsAsErrors = false
                checkDependencies = true
                baseline = rootProject.file("config/lint/lint-baseline.xml")
            }
        }
    }
}
