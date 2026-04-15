plugins {
    alias(libs.plugins.convention.android.library)
    id(libs.plugins.kotlin.plugin.parcelize.get().pluginId)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "com.velord.di"
}

dependencies {
    implementation(project(":infrastructure:config"))
    implementation(project(":model"))
    // Module Domain
    implementation(project(":domain:usecase-setting"))
    implementation(project(":domain:usecase-movie"))
    // Module Data Source
    implementation(project(":data:backend"))
    implementation(project(":data:datastore"))
    implementation(project(":data:appstate"))
    implementation(project(":data:gateway"))
    implementation(project(":data:db"))
    implementation(project(":data:os"))
    // Module UI
    implementation(project(":ui:sharedviewmodel"))
    // Module UI Feature
    implementation(project(":ui:feature-demo"))
    implementation(project(":ui:feature-camerarecording"))
    implementation(project(":ui:feature-bottomnavigation"))
    implementation(project(":ui:feature-setting"))
    implementation(project(":ui:feature-splash"))
    implementation(project(":ui:feature-flowsummator"))
    implementation(project(":ui:feature-movie"))
    implementation(project(":ui:feature-demo-dialog"))
    // Templates
    implementation(libs.bundles.kotlin.all)
    implementation(libs.bundles.androidx.module)
}
