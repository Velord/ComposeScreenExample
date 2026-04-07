plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.compose)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
}

android {
    namespace = "com.velord.refreshableimage"
}

dependencies {
    // Modules
    implementation(project(":infrastructure:util"))
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-ui"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.glance)
    implementation(libs.bundles.androidx.workmanager)
    implementation(libs.bundles.coil)
    // AndroidX
    implementation(libs.androidx.core.ktx)
    // Compose
    implementation(libs.bundles.compose.ui)
}
