plugins {
    alias(libs.plugins.convention.domain.usecase)
}

android {
    namespace = "com.velord.usecase.recording"
}

dependencies {
    api(libs.androidx.camera.video)
}
