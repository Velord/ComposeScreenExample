plugins {
    alias(libs.plugins.convention.feature.ui)
}

android {
    namespace = "com.velord.movie"
}

dependencies {
    // Module
    implementation(projects.model)
    implementation(projects.infrastructure.util)
    implementation(projects.core.coreResource)
    implementation(projects.core.coreUi)
    implementation(projects.ui.sharedviewmodel)
    implementation(projects.domain.usecaseMovie)
    // Template
    implementation(libs.bundles.compose.all)
    implementation(libs.bundles.coil)
    implementation(libs.androidx.constraint.compose)
    // 3-rd party
    implementation(libs.compose.scrollbar.nanihadesuka)
}
