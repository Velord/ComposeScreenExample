plugins {
    alias(libs.plugins.convention.feature.ui)
}

android {
    namespace = "com.velord.modifierdemo"
}

dependencies {
    // Modules
    implementation(project(":core:core-ui"))
    // Templates
    implementation(libs.bundles.compose.all)
}
