plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
}

android {
    namespace = "com.velord.resource"

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
    // Templates
    implementation(libs.bundles.kotlin.all)
    implementation(libs.bundles.androidx.module)
}