plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.dagger.hilt.get().pluginId)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.velord.sharedviewmodel"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Modules
    implementation(project(":util"))
    implementation(project(":datastore"))
    // Templates
    implementation(libs.androidx.core)
    implementation(libs.bundles.kotlin.core)
    // AndroidX
    implementation(libs.bundles.androidx.activity)
    implementation(libs.bundles.androidx.ktx)
    implementation(libs.bundles.androidx.navigation)
    // DI
    implementation(libs.bundles.dagger.all)
    kapt(libs.bundles.dagger.kapt)
    kapt(libs.hilt.compiler)
}