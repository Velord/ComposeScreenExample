import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    // Module
    implementation(project(":infrastructure:util"))
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-navigation"))
    implementation(project(":core:core-ui"))
    implementation(project(":ui:sharedviewmodel"))
    // Template
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.all)
    implementation(libs.bundles.kotlin.serialization)
    implementation(libs.bundles.voyager) // Refactor. Too much effort need to move to nav module
    // DI
    implementation(libs.bundles.koin)
    implementation(platform(libs.koin.bom))
    ksp(libs.koin.ksp)
    // Lib. Only Jetpack Navigation based on Fragment depends on it.
    implementation(libs.androidx.navigation.ui)
    // Third Party
    implementation(libs.velord.multiplebackstack)
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
    arg("KOIN_DEFAULT_MODULE","false")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}