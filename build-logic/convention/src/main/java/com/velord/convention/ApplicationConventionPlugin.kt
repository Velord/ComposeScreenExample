package com.velord.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the

// Make version catalog available in precompiled scripts
// https://github.com/gradle/gradle/issues/15383#issuecomment-1567461389
internal val Project.libs: LibrariesForLibs get() = the<LibrariesForLibs>()

internal fun DependencyHandlerScope.implementation(dependencyNotation: Any) {
    add("implementation", dependencyNotation)
}

internal fun DependencyHandlerScope.ksp(dependencyNotation: Any) {
    add("ksp", dependencyNotation)
}

internal fun Project.configureAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = libs.versions.targetApi.get().toInt()

        defaultConfig {
            minSdk = libs.versions.minApi.get().toInt()

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables {
                useSupportLibrary = true
            }

            resourceConfigurations += listOf("en")
        }
        compileOptions {
            targetCompatibility = JavaVersion.VERSION_24
        }
        buildFeatures {
            buildConfig = true
        }
    }
}

internal fun Project.configureCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
            viewBinding = true
        }
        dependencies {
            implementation(libs.bundles.compose.ui)
        }
    }
}

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.android.application.get().pluginId)
                apply(libs.plugins.kotlin.android.get().pluginId)
                apply(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
                apply(libs.plugins.kotlin.plugin.serialization.get().pluginId)
                apply(libs.plugins.kotlin.plugin.compose.get().pluginId)
                apply(libs.plugins.ksp.get().pluginId)
            }
            extensions.configure<ApplicationExtension> {
                configureAndroid(this)
            }
            configureCompose(extensions.getByType<ApplicationExtension>())
            dependencies {
                implementation(libs.bundles.kotlin.module)
                implementation(libs.bundles.androidx.module)
                // DI
                implementation(libs.bundles.koin)
                implementation(platform(libs.koin.bom))
                ksp(libs.koin.ksp)
                // Debug, Logging
                implementation(libs.bundles.logging)
            }
        }
    }
}
