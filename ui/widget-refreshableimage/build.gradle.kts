plugins {
    alias(libs.plugins.convention.widget.glance)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
}

android {
    namespace = "com.velord.ui.widget.refreshableimage"
}

dependencies {
    implementation(libs.bundles.coil)
    implementation(libs.androidx.core.ktx)
}
