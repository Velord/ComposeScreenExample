include(":app")

include(":util")
include(":model")

include(":backend")
include(":datastore")

include(":uicore")
include(":camerarecording")
include(":bottomnavigation")
include(":shapedemo")



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

