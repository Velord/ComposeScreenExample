package com.velord.buildlogic

internal enum class ProjectModule(val path: String) {
    MODEL(":model"),
    INFRASTRUCTURE_UTIL(":infrastructure:util"),
    CORE_RESOURCE(":core:core-resource"),
    CORE_UI(":core:core-ui"),
}
