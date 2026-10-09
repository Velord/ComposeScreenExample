package com.velord.core.navigation.fragment.entryPoint

import kotlinx.serialization.json.Json

enum class SettingsSourceFragment {
    SettingsGraph,
    CameraRecording,
    ;

    val isSettingsGraph: Boolean get() = this == SettingsGraph

    fun encode(): String = Json.encodeToString(this)

    companion object {
        const val ARGUMENT = "source"

        fun decode(payload: String): SettingsSourceFragment = Json.decodeFromString(payload)
    }
}
