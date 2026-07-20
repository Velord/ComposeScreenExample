package com.velord.buildlogic.model

internal enum class ProjectModule(val path: String) {
    MODEL(":model"),
    INFRASTRUCTURE_UTIL(":infrastructure:util"),
    CORE_RESOURCE(":core:core-resource"),
    CORE_UI(":core:core-ui"),
}