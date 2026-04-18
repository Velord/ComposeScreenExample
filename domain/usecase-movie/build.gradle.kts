plugins {
    alias(libs.plugins.convention.domain.usecase.kmp)
}

kotlin {
    android {
        namespace = "com.velord.usecase.movie"
    }

    sourceSets {
        desktopTest.dependencies {
            implementation(libs.junit)
            implementation(libs.coroutine.test)
            implementation(libs.mock)
            implementation(libs.mock.agent)
            implementation(libs.turbine)
        }
    }
}
