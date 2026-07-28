plugins {
    alias(libs.plugins.convention.widget.glance)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
}

android {
    namespace = "com.velord.ui.widget.refreshableimage"
}

dependencies {
    // Coil
    implementation(libs.bundles.coil)
    // AndroidX
    implementation(libs.androidx.core.ktx)
}
