plugins {
    alias(libs.plugins.convention.feature.ui.koin)
}

android {
    namespace = "com.velord.flowsummator"
}

dependencies {
    // Modules
    implementation(project(":infrastructure:util"))
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-ui"))
    implementation(project(":ui:sharedviewmodel"))
    // Templates
    implementation(libs.bundles.compose.all)
}
