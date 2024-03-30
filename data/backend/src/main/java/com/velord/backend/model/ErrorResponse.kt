package com.velord.backend.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val success: Boolean = true,
    val errors: Map<String, String> = emptyMap()
)