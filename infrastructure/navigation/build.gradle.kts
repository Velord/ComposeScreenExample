plugins {
    alias(libs.plugins.convention.multiplatform.library)
    alias(libs.plugins.multiplatform.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.velord.infrastructure.navigation"

        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Module Infrastructure
            implementation(projects.infrastructure.config)
            // Module Core
            implementation(projects.core.coreNavigation)
            implementation(projects.core.coreResource)
            implementation(projects.core.coreUi)
            // Module Data
            implementation(projects.data.localization)
            // Module UI
            implementation(projects.ui.sharedviewmodel)
            // Module UI Feature
            implementation(projects.ui.featureDemo)
            implementation(projects.ui.featureCamerarecording)
            implementation(projects.ui.featureBottomnavigation)
            implementation(projects.ui.featureSetting)
            implementation(projects.ui.featureDemoShape)
            implementation(projects.ui.featureDemoModifier)
            implementation(projects.ui.featureDemoMorph)
            implementation(projects.ui.featureDemoHintphonenumber)
            implementation(projects.ui.featureDemoDialog)
            implementation(projects.ui.featureFlowsummator)
            implementation(projects.ui.featureMovie)
            // Template
            implementation(libs.bundles.nav3)
            implementation(libs.bundles.voyager.navigation)
            // Compose
            implementation(libs.compose.resources)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.androidx.lifecycle.runtime.compose)
            // Kotlin
            implementation(libs.kotlin.datetime)
            implementation(libs.kotlin.serialization.json)
            // Voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            // Other
            implementation(libs.kermit)
        }

        androidMain.dependencies {
            // Module Infrastructure
            implementation(projects.infrastructure.util)
            // Module Core
            implementation(projects.core.coreUi)
            implementation(projects.core.coreNavigation)
            // Template
            implementation(libs.bundles.androidx.navigation)
            // AndroidX
            implementation(libs.androidx.activity.ktx)
            implementation(libs.androidx.collection)
            implementation(libs.androidx.fragment.ktx)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            // Compose
            implementation(libs.compose.ui.tooling.preview)
            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
            // Navigation Compose Destinations
            implementation(libs.compose.destinations)
        }

        commonTest.dependencies {
            // Testing
            implementation(libs.kotlin.test)
        }
    }
}

/*
Android KMP packages navigation XML from a generated resource directory. The plugin creates that
directory but does not copy src/androidMain/res into it, so this task prepares the packaging input
before packageAndroidMainResources runs.
*/
val prepareAndroidMainNavigationResources = tasks.register<Sync>(
    "prepareAndroidMainNavigationResources",
) {
    description = "Prepares the navigation XML resources for Android Main source set."
    // Android KMP maps navigation XML to this directory without producing it.
    dependsOn("generateAndroidMainEmptyResourceFiles")
    from("src/androidMain/res")
    into(layout.buildDirectory.dir("generated/updated_navigation_xml/androidMain/res"))
}

tasks.matching { task ->
    task.name == "packageAndroidMainResources"
}.configureEach {
    dependsOn(prepareAndroidMainNavigationResources)
}

// After migrating this module from Android-only to KMP, the generic `ksp` configuration
// became target-specific. Compose Destinations is used only by Android, so its processor
// must be registered with `kspAndroid` in the project-level dependencies block.
dependencies {
    kspAndroid(libs.compose.destinations.ksp)
}

ksp {
    arg("compose-destinations.moduleName", "navigation")
}
