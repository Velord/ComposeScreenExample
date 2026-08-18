plugins {
    alias(libs.plugins.convention.widget.glance)
}

android {
    namespace = "com.velord.ui.widget.counter"
}

dependencies {
    // Module Core
    implementation(projects.core.coreResource)
    // Module Domain
    implementation(projects.domain.usecaseSetting)
    // Koin
    implementation(libs.koin.core)
}
