plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.compose)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.navigation"
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
}

ksp {
    arg("compose-destinations.moduleName", "navigation")
}
