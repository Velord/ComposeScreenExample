package com.velord.infrastructure.config

enum class BuildEnvironment {
    Develop,
    Qa,
    Stage,
    Production;

    val isDevelopOrQa: Boolean get() = this == Develop || this == Qa
}
