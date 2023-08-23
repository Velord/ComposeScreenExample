plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
}

android {
    namespace = "com.velord.uicore"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    // Modules
    implementation(project(":util"))
    implementation(project(":model"))
    // Templates
    implementation(libs.bundles.kotlin.core)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.coil)
    // Compose
    implementation(libs.bundles.compose.core)
    implementation(libs.bundles.compose.foundation)
    implementation(libs.bundles.compose.material.all)
    implementation(libs.bundles.compose.accompanist.all)
    implementation(libs.bundles.compose.ui)
    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.appwidget)
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}

// https://slack-chats.kotlinlang.org/t/9025044/after-updating-my-project-to-kotlin-1-8-0-i-m-getting-the-fo
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}