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
include(":data:gatewaysetting")
// Domain
include(":domain:usecase-setting")
// UI
include(":ui:sharedviewmodel")
// UI Feature
include(":ui:feature-demo")
include(":ui:feature-camerarecording")
include(":ui:feature-bottomnavigation")
include(":ui:feature-demo-shape")
include(":ui:feature-demo-modifier")
include(":ui:feature-demo-morph")
include(":ui:feature-flowsummator")
include(":ui:feature-settings")
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
