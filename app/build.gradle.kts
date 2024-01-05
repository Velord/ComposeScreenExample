plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    id(libs.plugins.dagger.hilt.get().pluginId)
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
val hotfixVersion = 0
// Based on current CI BUILD_NUMBER
val buildNumber = System.getenv("BUILD_NUMBER")?.toIntOrNull() ?: hotfixVersion
// Doc says: max number is 2100000000
// Do not use auto numeration when value beyond edge
val maxSafeVersionCode = 1000000000
val calculatedVersionNumber = globalVersion * 100000 + majorVersion * 10000 + minorVersion * 1000 + fixVersion * 100 + buildNumber

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
        named("debug") {
            buildConfigField("Boolean", "IS_LOGGING_ENABLED", "true")
        }
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("Boolean", "IS_LOGGING_ENABLED", "false")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Module
    implementation(project(":resource"))

    implementation(project(":model"))
    implementation(project(":util"))

    implementation(project(":backend"))
    implementation(project(":datastore"))

    implementation(project(":sharedviewmodel"))
    implementation(project(":uicore"))

    implementation(project(":camerarecording"))
    implementation(project(":bottomnavigation"))
    implementation(project(":shapedemo"))
    implementation(project(":modifierdemo"))
    implementation(project(":flowsummator"))
    implementation(project(":settings"))

    implementation(project(":widgetrefreshableimage"))
    implementation(project(":widgetcounter"))
    // Templates
    implementation(libs.bundles.kotlin.all)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.voyager)
    // Compose
    implementation(libs.bundles.compose.all)
    // DI
    implementation(libs.bundles.dagger.all)
    kapt(libs.bundles.dagger.kapt)
    kapt(libs.hilt.compiler)
    // Other
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.glance.appwidget)
    // Only for test
    implementation(libs.bundles.androidx.all)
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}