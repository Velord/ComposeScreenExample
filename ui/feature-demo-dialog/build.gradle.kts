plugins {
    alias(libs.plugins.convention.feature.ui)
}

android {
    namespace = "com.velord.dialogdemo"
}

dependencies {
    // Modules
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-ui"))
    implementation(project(":ui:sharedviewmodel"))
    // Templates
    implementation(libs.bundles.compose.all)
}
