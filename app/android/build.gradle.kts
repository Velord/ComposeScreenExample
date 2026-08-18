import com.velord.buildlogic.model.BuildEnvironment
import com.velord.buildlogic.model.BuildType
import com.velord.buildlogic.util.AppVersion

plugins {
    alias(libs.plugins.convention.android.application)
    alias(libs.plugins.convention.android.compose)
    alias(libs.plugins.convention.android.viewbinding)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.gms.services)
    alias(libs.plugins.google.firebase.crashlytic)
    alias(libs.plugins.dependency.guard)
}

dependencyGuard {
    val buildVariant = BuildEnvironment.Production.variantName(buildType = BuildType.Release)
    configuration("${buildVariant}RuntimeClasspath")
}

android {
    namespace = "com.velord.composescreenexample"

    defaultConfig {
        applicationId = "com.velord.composescreenexample"

        targetSdk = libs.versions.targetApi.get().toInt()

        versionCode = AppVersion.versionCode
        versionName = AppVersion.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName(BuildType.Release.value) {
            signingConfig = signingConfigs.getByName(BuildType.Debug.value)
        }
        named(BuildType.Release.value) {
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
        create(BuildEnvironment.Develop.value) {
            dimension = "environment"
            manifestPlaceholders["enableCrashReporting"] = false
            applicationIdSuffix = ".${BuildEnvironment.Develop.value}"

            resourceConfigurations.add("xxxhdpi")
        }
        create(BuildEnvironment.Qa.value) {
            dimension = "environment"
            manifestPlaceholders["enableCrashReporting"] = true
            applicationIdSuffix = ".${BuildEnvironment.Qa.value}"
        }

        create(BuildEnvironment.Stage.value) {
            dimension = "environment"
            manifestPlaceholders["enableCrashReporting"] = true
            applicationIdSuffix = ".${BuildEnvironment.Stage.value}"
        }

        create(BuildEnvironment.Production.value) {
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
    // Module Domain
    implementation(projects.domain.usecaseSetting)
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

val navigationResourcePreparation = project(":infrastructure:navigation").tasks.matching { task ->
    task.name == "prepareAndroidMainNavigationResources"
}

tasks.matching { task ->
    task.name.endsWith("NavigationResources")
}.configureEach {
    // Compose Destinations reads the generated Android KMP navigation resources directly.
    dependsOn(navigationResourcePreparation)
}
