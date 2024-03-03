plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
}

android {
    namespace = "com.velord.windyappflowsummator"

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
    implementation(project(":util"))
    implementation(project(":core-ui"))
    implementation(project(":sharedviewmodel"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.activity)
    implementation(libs.bundles.androidx.lifecycle.runtime)
    implementation(libs.bundles.androidx.lifecycle.viewmodel)
    implementation(libs.bundles.androidx.ktx)
    implementation(libs.bundles.voyager)
    implementation(libs.bundles.compose.all)
}