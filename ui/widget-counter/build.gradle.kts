plugins {
    alias(libs.plugins.convention.widget.glance)
}

android {
    namespace = "com.velord.ui.widget.counter"
}

dependencies {
    // Module Model
    implementation(projects.model)
    // Module Data
    implementation(projects.data.localization)
}
