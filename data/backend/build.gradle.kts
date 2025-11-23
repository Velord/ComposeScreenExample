plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.velord.backend"

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
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    // Module
    implementation(project(":model"))
    implementation(project(":infrastructure:util"))
    implementation(project(":data:datastore"))
    // Templates
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.kotlin.core)
    implementation(libs.bundles.network.all)
    // DI
    implementation(libs.bundles.koin)
    implementation(platform(libs.koin.bom))
    ksp(libs.koin.ksp)
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
    arg("KOIN_DEFAULT_MODULE","false")
}