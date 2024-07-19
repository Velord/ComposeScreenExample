include(":app")
// Universal
include(":util")
include(":model")
// Core
include(":core:core-ui")
include(":core:core-navigation")
include(":core:core-resource")
// Data Source
include(":data:backend")
include(":data:datastore")
include(":data:appstate")
// Data Gateway
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

rootProject.name = "ComposeScreenExample"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
    }
}
