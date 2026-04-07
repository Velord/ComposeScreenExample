plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.compose)
    alias(libs.plugins.convention.android.viewbinding)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.feature.demo"
}

dependencies {
    // Modules Infrastructure
    implementation(project(":infrastructure:util"))
    implementation(project(":infrastructure:config"))
    // Modules Core
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-navigation"))
    implementation(project(":core:core-ui"))
    // Modules Ui
    implementation(project(":ui:sharedviewmodel"))
    implementation(project(":ui:feature-bottomnavigation"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.all)
}
