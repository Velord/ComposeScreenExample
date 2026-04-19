plugins {
    alias(libs.plugins.convention.feature.ui)
}

android {
    namespace = "com.velord.modifierdemo"
}

dependencies {
    // Module
    implementation(projects.core.coreUi)
    // Template
    implementation(libs.bundles.compose.all)
}
