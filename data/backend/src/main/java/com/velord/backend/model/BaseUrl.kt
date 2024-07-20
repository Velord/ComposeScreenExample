package com.velord.backend.model

data class BaseUrl(
    val protocol: String,
    val host: String,
) {
    val full: String
        get() = "$protocol://$host"
}