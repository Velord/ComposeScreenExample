plugins {
    alias(libs.plugins.convention.domain.usecase)
}

kotlin {
    android {
        namespace = "com.velord.usecase.event"
    }
}
