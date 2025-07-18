plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.velord.model"

    compileSdk = libs.versions.targetApi.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minApi.get().toInt()
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
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_24
    }
}

dependencies {
    // Templates
    implementation(libs.bundles.androidx.activity)
    implementation(libs.bundles.kotlin.core)
    // Json
    implementation(libs.kotlin.serialization.json)
    // Test
    testImplementation(libs.bundles.test)
}

