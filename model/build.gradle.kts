plugins {
    alias(libs.plugins.convention.android.library)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.velord.model"
}

dependencies {
    // Templates
    implementation(libs.bundles.androidx.activity)
    implementation(libs.bundles.kotlin.core)
    // Json
    implementation(libs.kotlin.serialization.json)
    // Test
    testImplementation(libs.bundles.test)
}
