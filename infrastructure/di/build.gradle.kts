plugins {
    alias(libs.plugins.convention.multiplatform.library)
}

kotlin {
    android {
        namespace = "com.velord.infrastructure.di"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.infrastructure.config)
            implementation(projects.model)
            // Module Domain
            implementation(projects.domain.usecaseSetting)
            implementation(projects.domain.usecaseMovie)
            implementation(projects.domain.usecaseCamera)
            implementation(projects.domain.usecaseEvent)
            // Module Data Source
            implementation(projects.data.backend)
            implementation(projects.data.datastore)
            implementation(projects.data.appstate)
            implementation(projects.data.gateway)
            implementation(projects.data.db)
            implementation(projects.data.os)
            // Module UI
            implementation(projects.ui.sharedviewmodel)
            // Module UI Feature
            implementation(projects.ui.featureDemo)
            implementation(projects.ui.featureCamerarecording)
            implementation(projects.ui.featureBottomnavigation)
            implementation(projects.ui.featureSetting)
            implementation(projects.ui.featureSplash)
            implementation(projects.ui.featureFlowsummator)
            implementation(projects.ui.featureMovie)
            implementation(projects.ui.featureDemoDialog)
            // Template
            implementation(libs.bundles.kotlin.all)
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
        }
        androidMain.dependencies {
            implementation(projects.core.coreUi)
        }
        desktopTest.dependencies {
            implementation(projects.infrastructure.config)
            implementation(libs.kotlin.test)
            implementation(libs.koin.test)
        }
    }
}

tasks.register("koinGraphTest") {
    group = "verification"
    description = "Runs Koin graph verification for the app module roster."
    dependsOn("desktopTest")
}

tasks.named("check") {
    dependsOn("koinGraphTest")
}
