pluginManagement {
    // has name 'build-logic' which is the same as a project of the main build.
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        maven("https://androidx.dev/storage/compose-compiler/repository") {
            name = "Compose Compiler Snapshots"
            content { includeGroup("androidx.compose.compiler") }
        }
        mavenCentral()
        // Does not work
//        exclusiveContent {
//            forRepository { maven("https://jitpack.io") { name = "JitPack" } }
//            filter { includeGroup("com.github") }
//        }
        maven("https://oss.sonatype.org/content/repositories/snapshots/") {
            name = "Sonatype snapshots"
            mavenContent {
                snapshotsOnly()
            }
        }
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

rootProject.name = "ComposeScreenExample"

include(":app")
// Model
include(":model")
// Specific
include(":infrastructure:util")
include(":infrastructure:navigation")
include(":infrastructure:di")
// Core
include(":core:core-ui")
include(":core:core-navigation")
include(":core:core-resource")
// Data Source
include(":data:backend")
include(":data:datastore")
include(":data:appstate")
include(":data:db")
include(":data:gateway")
// Domain
include(":domain:usecase-setting")
include(":domain:usecase-movie")
// UI
include(":ui:sharedviewmodel")
// UI Feature
include(":ui:feature-splash")
include(":ui:feature-camerarecording")
include(":ui:feature-bottomnavigation")
include(":ui:feature-demo-hintphonenumber")
include(":ui:feature-demo")
include(":ui:feature-demo-shape")
include(":ui:feature-demo-modifier")
include(":ui:feature-demo-morph")
include(":ui:feature-flowsummator")
include(":ui:feature-settings")
include(":ui:feature-movie")
// UI Widget
include(":ui:widget-refreshableimage")
include(":ui:widget-counter")
include(":build-logic:convention")
