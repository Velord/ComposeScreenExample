plugins {
    alias(libs.plugins.convention.feature.ui)
}

android {
    namespace = "com.velord.hintphonenumber"
}

dependencies {
    // Modules
    implementation(project(":infrastructure:util"))
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-ui"))
    // Templates
    implementation(libs.bundles.compose.all)
}
