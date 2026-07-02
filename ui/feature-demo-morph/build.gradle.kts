plugins {
    alias(libs.plugins.convention.feature.ui)
}

android {
    namespace = "com.velord.ui.feature.demo.morph"
}

dependencies {
    // Module
    implementation(projects.core.coreUi)
    // Template
    implementation(libs.bundles.ui)
}
