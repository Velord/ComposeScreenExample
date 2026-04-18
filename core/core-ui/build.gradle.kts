plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.compose)
    alias(libs.plugins.convention.android.viewbinding)
}

android {
    namespace = "com.velord.core.ui"
}

dependencies {
    // Module
    implementation(projects.model)
    implementation(projects.infrastructure.util)
    implementation(projects.core.coreResource)
    implementation(projects.ui.sharedviewmodel)
    // Template
    implementation(libs.bundles.kotlin.core)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.ui)
    // Lib
    implementation(libs.androidx.navigation.ui) // Material Dialog lib
    // Compose
    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.appwidget)
}
