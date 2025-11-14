import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.velord.navigation"

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
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Module
    implementation(project(":model"))
    implementation(project(":infrastructure:util"))
    implementation(project(":infrastructure:config"))
    // Module Core
    implementation(project(":core:core-ui"))
    implementation(project(":core:core-navigation"))
    implementation(project(":core:core-resource"))
    // Module UI
    implementation(project(":ui:sharedviewmodel"))
    // Module UI Feature
    implementation(project(":ui:feature-demo"))
    implementation(project(":ui:feature-camerarecording"))
    implementation(project(":ui:feature-bottomnavigation"))
    implementation(project(":ui:feature-setting"))
    implementation(project(":ui:feature-splash"))
    implementation(project(":ui:feature-demo-shape"))
    implementation(project(":ui:feature-demo-modifier"))
    implementation(project(":ui:feature-demo-morph"))
    implementation(project(":ui:feature-demo-hintphonenumber"))
    implementation(project(":ui:feature-demo-dialog"))
    implementation(project(":ui:feature-flowsummator"))
    implementation(project(":ui:feature-movie"))
    // Templates
    implementation(libs.bundles.kotlin.all)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.androidx.navigation.all)
    implementation(libs.bundles.compose.all)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.kotlin.serialization)
    // Navigation 3-rd Party
    // Navigation Voyager
    implementation(libs.bundles.voyager)
    // Navigation Compose Destinations
    implementation(libs.bundles.compose.destinations)
    ksp(libs.compose.destinations.ksp)
    // DI
    implementation(libs.bundles.koin)
    implementation(platform(libs.koin.bom))
    ksp(libs.koin.ksp)
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
    arg("KOIN_DEFAULT_MODULE","false")
    arg("compose-destinations.moduleName", "navigation")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}