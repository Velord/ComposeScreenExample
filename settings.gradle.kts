include(":app")

include(":util")
include(":model")

include(":backend")
include(":datastore")

include(":uicore")
include(":camerarecording")
include(":bottomnavigation")



rootProject.name = "ComposeScreenExample"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
        jcenter()
    }
}

