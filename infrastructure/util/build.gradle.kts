plugins {
    id("velord.android.library")
    id("velord.android.compose")
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.velord.util"
}

dependencies {
    // Template
    implementation(libs.bundles.kotlin.module)
    implementation(libs.bundles.androidx.module)
    implementation(libs.bundles.network.all)
    implementation(libs.bundles.androidx.credentials)
    implementation(libs.bundles.google.gms)
    // Lib
    implementation(libs.androidx.glance.appwidget)
}
