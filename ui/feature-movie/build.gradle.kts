import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.velord.movie"

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

composeCompiler {
    featureFlags = setOf(
        ComposeFeatureFlag.OptimizeNonSkippingGroups
    )
}

dependencies {
    // Modules
    implementation(project(":model"))
    implementation(project(":infrastructure:util"))
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-ui"))
    implementation(project(":ui:sharedviewmodel"))
    implementation(project(":domain:usecase-movie"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.all)
    implementation(libs.bundles.coil)
    implementation(libs.androidx.constraint.compose)
    // 3-rd party
    implementation(libs.compose.scrollbar.nanihadesuka)
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
    arg("KOIN_DEFAULT_MODULE","false")
}