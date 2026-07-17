plugins {
    alias(libs.plugins.convention.android.application)
    alias(libs.plugins.convention.android.compose)
    alias(libs.plugins.convention.android.viewbinding)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.google.gms.services)
    alias(libs.plugins.google.firebase.crashlytic)
    alias(libs.plugins.dependency.guard)
}

dependencyGuard {
    configuration("productionReleaseRuntimeClasspath")
}

// When app incompatible with previous version change this value
val globalVersion = 1
// When you create huge feature(or many) release change this value
val majorVersion = 2
// When you create feature release change this value
val minorVersion = 0
// When you create fix change this value
val fixVersion = 0
// When you create quick fix from master branch change this value
val hotfixVersion = 0
// Based on current CI BUILD_NUMBER
val buildNumber = System.getenv("BUILD_NUMBER")?.toIntOrNull() ?: hotfixVersion
// Doc says: max number is 2100000000
// Do not use auto numeration when value beyond edge
val maxSafeVersionCode = 1000000000
val calculatedVersionNumber = globalVersion * 100000 +
        majorVersion * 10000 +
        minorVersion * 1000 +
        fixVersion * 100 +
        buildNumber

android {
    namespace = "com.velord.composescreenexample"

    defaultConfig {
        applicationId = "com.velord.composescreenexample"

        targetSdk = libs.versions.targetApi.get().toInt()

        //Don't use number greater than maxSafeVersionCode
        val isLessThanMax = calculatedVersionNumber < maxSafeVersionCode
        versionCode = if (isLessThanMax) calculatedVersionNumber else 0
        versionName = "$globalVersion.$majorVersion.$minorVersion"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        androidResources.localeFilters += listOf("en")
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions.add("environment")
    productFlavors {
        create("develop") {
            dimension = "environment"
            manifestPlaceholders["enableCrashReporting"] = false
            applicationIdSuffix = ".develop"

            resourceConfigurations.add("xxxhdpi")
        }
        create("qa") {
            dimension = "environment"
            manifestPlaceholders["enableCrashReporting"] = true
            applicationIdSuffix = ".develop"
        }

        create("stage") {
            dimension = "environment"
            manifestPlaceholders["enableCrashReporting"] = true
            applicationIdSuffix = ".stage"
        }

        create("production") {
            dimension = "environment"
            manifestPlaceholders["enableCrashReporting"] = true
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    implementation(projects.model)
    // Module Infrastructure
    implementation(projects.infrastructure.util)
    implementation(projects.infrastructure.navigation)
    implementation(projects.infrastructure.di)
    implementation(projects.infrastructure.config)
    // Module Core
    implementation(projects.core.coreUi)
    implementation(projects.core.coreNavigation)
    implementation(projects.core.coreResource)
    // Module Data
    implementation(projects.data.os)
    implementation(projects.data.appstate)
    // Module UI
    implementation(projects.ui.sharedviewmodel)
    // Module UI Feature
    implementation(projects.ui.featureBottomnavigation)
    implementation(projects.ui.featureSplash)
    // Module UI Widget
    implementation(projects.ui.widgetRefreshableimage)
    implementation(projects.ui.widgetCounter)
    // Template
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.ui.core)
    // Koin annotations
    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    // Navigation
    implementation(libs.androidx.navigation.fragment)
    // Tool
    coreLibraryDesugaring(libs.android.desugar)
    // Other
    // Test libs.versions.toml
//    implementation(libs.bundles.android.all)
//    implementation(libs.bundles.androidx.all)
//    implementation(libs.bundles.kotlin.all)
//    implementation(libs.bundles.compose.all)
//    implementation(libs.bundles.compose.thirdparty)
//    implementation(libs.bundles.logging)
//    implementation(libs.bundles.google.all)
//    implementation(libs.bundles.google.firebase)
//    implementation(platform(libs.google.firebase.bom))
}

// Making optimized out variables visible in the IDE.
kotlin {
    compilerOptions {
        if (System.getProperty("idea.active") == "true") {
            println("Enable coroutine debugging")
            freeCompilerArgs.add("-Xdebug")
        }
    }
}
