plugins {
    alias(libs.plugins.convention.domain.usecase)
}

android {
    namespace = "com.velord.usecase.movie"
}

dependencies {
    // Test
    testImplementation(libs.junit)
    testImplementation(libs.bundles.test)
}
