plugins {
    alias(libs.plugins.convention.feature.ui)
}

android {
    namespace = "com.velord.dialogdemo"
}

dependencies {
    // Module
    implementation(projects.core.coreResource)
    implementation(projects.core.coreUi)
    implementation(projects.ui.sharedviewmodel)
    // Template
    implementation(libs.bundles.compose.all)
}
