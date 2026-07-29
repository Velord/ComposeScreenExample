plugins {
    alias(libs.plugins.convention.domain.usecase)
    alias(libs.plugins.mokkery)
}

kotlin {
    android {
        namespace = "com.velord.usecase.movie"
    }

    sourceSets {
        commonTest.dependencies {
            // Testing
            implementation(libs.bundles.test.kmp)
        }
    }
}
