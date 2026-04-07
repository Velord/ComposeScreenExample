plugins {
    alias(libs.plugins.convention.feature.ui.koin)
}

android {
    namespace = "com.velord.setting"
}

dependencies {
    // Modules
    implementation(project(":model"))
    implementation(project(":core:core-navigation"))
    implementation(project(":core:core-ui"))
    implementation(project(":core:core-resource"))
    implementation(project(":ui:sharedviewmodel"))
    implementation(project(":ui:feature-bottomnavigation"))
    // Templates
    implementation(libs.bundles.compose.all)
}
