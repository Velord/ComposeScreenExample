plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.velord.db"

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
        targetCompatibility = JavaVersion.VERSION_17
        // Does not compile without this.
        // error: cannot find symbol
        sourceCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Modules
    implementation(project(":model"))
    // Templates
    // DI
    implementation(libs.bundles.koin)
    implementation(platform(libs.koin.bom))
    ksp(libs.koin.ksp)
    // Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.androidx.room.coroutines)
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
    arg("KOIN_DEFAULT_MODULE","false")
}