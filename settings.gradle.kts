include(":app")

include(":resource")
include(":core-navigation")

include(":util")
include(":model")

include(":backend")
include(":datastore")

include(":sharedviewmodel")
include(":core-ui")

include(":feature-demo")
include(":feature-camerarecording")
include(":feature-bottomnavigation")
include(":feature-demo-shape")
include(":feature-demo-modifier")
include(":feature-flowsummator")
include(":feature-settings")

include(":widget-refreshableimage")
include(":widget-counter")

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
