include(":app")

include(":resource")
include(":navigation-core")

include(":util")
include(":model")

include(":backend")
include(":datastore")

include(":sharedviewmodel")
include(":uicore")

include(":feature-demo")
include(":camerarecording")
include(":bottomnavigation")
include(":shapedemo")
include(":modifierdemo")
include(":flowsummator")
include(":settings")

include(":widgetrefreshableimage")
include(":widgetcounter")

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
