plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.velord.bottomnavigation"

    compileSdk = libs.versions.targetApi.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minApi.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Modules
    implementation(project(":infrastructure:util"))
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-navigation"))
    implementation(project(":core:core-ui"))
    implementation(project(":ui:sharedviewmodel"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.all)
    implementation(libs.bundles.kotlin.serialization)
    // DI
    implementation(libs.bundles.koin)
    implementation(platform(libs.koin.bom))
    ksp(libs.koin.ksp)
    // Navigation
    implementation(libs.bundles.voyager)
    // Third Party
    implementation(libs.velord.multiplebackstack)
}

composeCompiler {
    enableStrongSkippingMode = true
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
    arg("KOIN_DEFAULT_MODULE","false")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}