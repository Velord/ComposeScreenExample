plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.ksp)
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
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Modules
    implementation(project(":resource"))
    implementation(project(":core-navigation"))
    implementation(project(":util"))
    implementation(project(":core-ui"))
    implementation(project(":feature-settings"))
    implementation(project(":sharedviewmodel"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.activity)
    implementation(libs.bundles.androidx.lifecycle.runtime)
    implementation(libs.bundles.androidx.ktx)
    implementation(libs.bundles.androidx.navigation)
    implementation(libs.google.guava)
    implementation(libs.bundles.voyager)
    // Compose
    implementation(libs.bundles.compose.core)
    implementation(libs.bundles.compose.foundation)
    implementation(libs.bundles.compose.material.all)
    implementation(libs.bundles.compose.ui)
    implementation(libs.bundles.compose.accompanist.core)
    // DI
    implementation(libs.bundles.koin.core)
    ksp(libs.koin.ksp)
    // Third Party
    implementation(libs.velord.multiplebackstack)
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
    arg("KOIN_DEFAULT_MODULE","false")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}