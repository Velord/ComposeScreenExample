package com.velord.util.exception

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val success: Boolean = true,
    val errors: Map<String, String> = emptyMap()
)