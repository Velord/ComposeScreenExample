plugins {
    alias(libs.plugins.convention.feature.ui)
}

android {
    namespace = "com.velord.demomorph"
}

dependencies {
    // Module
    implementation(projects.core.coreUi)
    // Template
    implementation(libs.bundles.ui)
}
