package com.velord.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureApplicationAndroid(applicationExtension: ApplicationExtension) {
    applicationExtension.apply {
        compileSdk = version("targetApi").toInt()

        defaultConfig {
            minSdk = version("minApi").toInt()
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

internal fun Project.configureLibraryAndroid(libraryExtension: LibraryExtension) {
    libraryExtension.apply {
        compileSdk = version("targetApi").toInt()

        defaultConfig {
            minSdk = version("minApi").toInt()
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
