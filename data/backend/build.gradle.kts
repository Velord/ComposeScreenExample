plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.kotlin.plugin.serialization)
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
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Module
    implementation(project(":model"))
    implementation(project(":util"))
    implementation(project(":data:datastore"))
    // Templates
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.kotlin.core)
    implementation(libs.bundles.network.all)
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}