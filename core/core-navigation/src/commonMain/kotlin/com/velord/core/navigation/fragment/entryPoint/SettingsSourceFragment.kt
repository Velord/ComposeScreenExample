package com.velord.core.navigation.fragment.entryPoint

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
enum class SettingsSourceFragment {
    SettingsGraph,
    CameraRecording,
    ;

    fun encode(): String = Json.encodeToString(this)

    companion object {

        const val ARGUMENT = "source"

        fun decode(payload: String): SettingsSourceFragment = Json.decodeFromString(payload)
    }
}
