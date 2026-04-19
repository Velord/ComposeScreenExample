plugins {
    alias(libs.plugins.convention.feature.ui)
}

android {
    namespace = "com.velord.shapedemo"
}

dependencies {
    // Module
    implementation(projects.core.coreUi)
    // Template
    implementation(libs.bundles.compose.all)
}
