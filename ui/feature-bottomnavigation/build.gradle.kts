plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.compose)
    alias(libs.plugins.convention.android.viewbinding)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.bottomnavigation"
}

dependencies {
    // Module
    implementation(project(":infrastructure:util"))
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-navigation"))
    implementation(project(":core:core-ui"))
    implementation(project(":ui:sharedviewmodel"))
    // Template
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.compose.all)
    implementation(libs.bundles.kotlin.serialization)
    implementation(libs.bundles.voyager) // Refactor. Too much effort need to move to nav module
    // Lib. Only Jetpack Navigation based on Fragment depends on it.
    implementation(libs.androidx.navigation.ui)
    // Third Party
    implementation(libs.velord.multiplebackstack)
}
