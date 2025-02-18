plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.kotlin.plugin.serialization)
}

// When app incompatible with previous version change this value
val globalVersion = 1
// When you create huge feature(or many) release change this value
val majorVersion = 0
// When you create feature release change this value
val minorVersion = 0
// When you create fix change this value
val fixVersion = 0
// When you create quick fix from master branch change this value
val hotfixVersion = 2
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

    compileSdk = libs.versions.targetApi.get().toInt()

    defaultConfig {
        applicationId = "com.velord.composescreenexample"

        minSdk = libs.versions.minApi.get().toInt()
        targetSdk = libs.versions.targetApi.get().toInt()

        //Don"t use number greater than maxSafeVersionCode
        val isLessThanMax = calculatedVersionNumber < maxSafeVersionCode
        versionCode = if (isLessThanMax) calculatedVersionNumber else 0
        versionName = "$globalVersion.$majorVersion.$minorVersion"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        resourceConfigurations += listOf("en")
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
        named("debug") {
            buildConfigField("Boolean", "IS_LOGGING_ENABLED", "true")
            buildConfigField(
                "com.velord.navigation.NavigationLib",
                "NAVIGATION_LIB",
                "com.velord.navigation.NavigationLib.Destinations"
            )
        }
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("Boolean", "IS_LOGGING_ENABLED", "false")
            buildConfigField(
                "com.velord.navigation.NavigationLib",
                "NAVIGATION_LIB",
                "com.velord.navigation.NavigationLib.Compose"
            )
        }
    }

    flavorDimensions.add("environment")
    productFlavors {
        val baseUrl = "https://google.com"
        val currentVersion = globalVersion * 100000 + majorVersion * 10000 + minorVersion * 1000 + fixVersion * 100
        create("develop") {
            dimension = "environment"
            manifestPlaceholders["enableCrashReporting"] = false
            applicationIdSuffix = ".develop"
            buildConfigField("String", "BASE_URL", "\"${baseUrl}\"")
            buildConfigField("String", "CURRENT_VERSION", "\"${currentVersion}\"")

            resourceConfigurations += listOf("en", "xxxhdpi")
        }
        create("qa") {
            dimension = "environment"
            manifestPlaceholders["enableCrashReporting"] = true
            applicationIdSuffix = ".develop"
            buildConfigField("String", "BASE_URL", "\"${baseUrl}\"")
            buildConfigField("String", "CURRENT_VERSION", "\"${currentVersion}\"")
        }

        create("stage") {
            dimension = "environment"
            manifestPlaceholders["enableCrashReporting"] = true
            applicationIdSuffix = ".stage"
            buildConfigField("String", "BASE_URL", "\"${baseUrl}\"")
            buildConfigField("String", "CURRENT_VERSION", "\"${currentVersion}\"")
        }

        create("production") {
            dimension = "environment"
            manifestPlaceholders["enableCrashReporting"] = true
            buildConfigField("String", "BASE_URL", "\"${baseUrl}\"")
            buildConfigField("String", "CURRENT_VERSION", "\"${currentVersion}\"")
        }
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":model"))
    // Module Infrastructure
    implementation(project(":infrastructure:util"))
    implementation(project(":infrastructure:navigation"))
    implementation(project(":infrastructure:di"))
    // Module Core
    implementation(project(":core:core-ui"))
    implementation(project(":core:core-navigation"))
    implementation(project(":core:core-resource"))
    // Module UI
    implementation(project(":ui:sharedviewmodel"))
    // Module UI Feature
    implementation(project(":ui:feature-bottomnavigation"))
    implementation(project(":ui:feature-splash"))
    // Module UI Widget
    implementation(project(":ui:widget-refreshableimage"))
    implementation(project(":ui:widget-counter"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.ui)
    // DI
    implementation(libs.bundles.koin)
    implementation(platform(libs.koin.bom))
    ksp(libs.koin.ksp)
    // Other
    implementation(libs.androidx.glance.appwidget)
    // Test libs.versions.toml
    implementation(libs.bundles.androidx.all)
    implementation(libs.bundles.kotlin.all)
    implementation(libs.bundles.compose.all)
    implementation(libs.bundles.compose.thirdparty)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.google.all)
}

composeCompiler {
    enableStrongSkippingMode = true
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
    arg("KOIN_DEFAULT_MODULE","false")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    compilerOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}