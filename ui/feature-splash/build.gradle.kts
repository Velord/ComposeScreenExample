plugins {
    alias(libs.plugins.convention.feature.ui)
}

android {
    namespace = "com.velord.splash"
}

dependencies {
    // Modules
    implementation(project(":model"))
    implementation(project(":core:core-resource"))
    implementation(project(":core:core-ui"))
    // Templates
    implementation(libs.bundles.compose.all)
    // Libs
    implementation(libs.androidx.core.splashscreen)
}
