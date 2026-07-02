plugins {
    alias(libs.plugins.convention.feature.ui)
}

android {
    namespace = "com.velord.ui.feature.demo.hintphonenumber"
}

dependencies {
    // Module
    implementation(projects.infrastructure.util)
    implementation(projects.core.coreResource)
    implementation(projects.core.coreUi)
    // Template
    implementation(libs.bundles.compose.all)
}
