plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.plugin.compose)
}

android {
    namespace = "com.velord.settings"

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
    implementation(project(":model"))
    implementation(project(":core:core-navigation"))
    implementation(project(":core:core-ui"))
    implementation(project(":core:core-resource"))
    implementation(project(":ui:sharedviewmodel"))
    implementation(project(":ui:feature-bottomnavigation"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.all)
    // DI
    implementation(libs.bundles.koin)
    implementation(platform(libs.koin.bom))
    ksp(libs.koin.ksp)
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
    arg("KOIN_DEFAULT_MODULE","false")
}