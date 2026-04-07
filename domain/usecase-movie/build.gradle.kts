plugins {
    alias(libs.plugins.convention.android.library)
}

android {
    namespace = "com.velord.usecase.movie"
}

dependencies {
    // Modules
    implementation(project(":model"))
    // Templates
    implementation(libs.bundles.kotlin.module)
    // Test
    testImplementation(libs.junit)
    testImplementation(libs.bundles.test)
}
