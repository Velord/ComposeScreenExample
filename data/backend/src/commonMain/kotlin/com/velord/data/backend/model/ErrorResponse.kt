package com.velord.data.backend.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val success: Boolean = true,
    @SerialName("errors")
    val errorRoster: Map<String, String> = emptyMap(),
)
