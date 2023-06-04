plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.velord.camerarecording"

    compileSdk = libs.versions.targetApi.get().toInt()

    defaultConfig {
        targetSdk = libs.versions.targetApi.get().toInt()
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
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    // Modules
    implementation(project(":model"))
    implementation(project(":util"))
    implementation(project(":uicore"))
    // Templates
    implementation(libs.bundles.kotlin.module.app)
    implementation(libs.bundles.androidx.module.app)
    implementation(libs.bundles.androidx.camera)
    // Compose
    implementation(libs.bundles.compose.core)
    implementation(libs.bundles.compose.foundation)
    implementation(libs.bundles.compose.material)
    implementation(libs.bundles.compose.accompanist)
    // DI
    implementation(libs.bundles.dagger)
    kapt(libs.bundles.dagger.kapt)
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.compiler)
}

// https://slack-chats.kotlinlang.org/t/9025044/after-updating-my-project-to-kotlin-1-8-0-i-m-getting-the-fo
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}