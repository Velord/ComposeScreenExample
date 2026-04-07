plugins {
    id("velord.android.library")
    id("velord.android.compose")
    id("velord.android.viewbinding")
}

android {
    namespace = "com.velord.core.ui"
}

dependencies {
    // Module
    implementation(project(":model"))
    implementation(project(":infrastructure:util"))
    implementation(project(":core:core-resource"))
    implementation(project(":ui:sharedviewmodel"))
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
