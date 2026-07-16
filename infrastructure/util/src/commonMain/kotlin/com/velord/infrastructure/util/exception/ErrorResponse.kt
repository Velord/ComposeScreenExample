package com.velord.infrastructure.util.exception

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val success: Boolean = true,
    @SerialName("errors")
    val errorRoster: Map<String, String> = emptyMap()
)
