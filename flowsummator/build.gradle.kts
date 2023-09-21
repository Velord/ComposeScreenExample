plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
}

android {
    namespace = "com.example.windyappflowsummator"

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
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    // Modules
    implementation(project(":util"))
    implementation(project(":uicore"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.activity)
    implementation(libs.bundles.androidx.lifecycle.runtime)
    implementation(libs.bundles.androidx.ktx)
    // Compose
    implementation(libs.bundles.compose.core)
    implementation(libs.bundles.compose.foundation)
    implementation(libs.bundles.compose.material.third)
    implementation(libs.bundles.compose.ui)
    implementation(libs.bundles.compose.accompanist.core)
}

// https://slack-chats.kotlinlang.org/t/9025044/after-updating-my-project-to-kotlin-1-8-0-i-m-getting-the-fo
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}