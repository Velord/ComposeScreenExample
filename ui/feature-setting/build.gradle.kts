plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.compose)
    alias(libs.plugins.convention.android.viewbinding)
    alias(libs.plugins.convention.koin)
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
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.all)
}
