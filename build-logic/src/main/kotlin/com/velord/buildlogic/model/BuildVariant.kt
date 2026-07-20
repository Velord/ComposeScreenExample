package com.velord.buildlogic.model

enum class BuildEnvironment(val value: String) {
    Develop("develop"),
    Qa("qa"),
    Stage("stage"),
    Production("production");

    fun variantName(buildType: BuildType): String =
        value + buildType.value.replaceFirstChar(Char::uppercase)
}

enum class BuildType(val value: String) {
    Debug("debug"),
    Release("release"),
}
