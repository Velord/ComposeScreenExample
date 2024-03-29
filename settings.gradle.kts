include(":app")
// Data
// Domain
// UI
// Core
include(":util")
include(":model")

include(":data:resource")
include(":data:backend")
include(":data:datastore")

include(":domain:usecase-setting")

include(":core-ui")
include(":core-navigation")

include(":ui:sharedviewmodel")

include(":ui:feature-demo")
include(":ui:feature-camerarecording")
include(":ui:feature-bottomnavigation")
include(":ui:feature-demo-shape")
include(":ui:feature-demo-modifier")
include(":ui:feature-demo-morph")
include(":ui:feature-flowsummator")
include(":ui:feature-settings")

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
