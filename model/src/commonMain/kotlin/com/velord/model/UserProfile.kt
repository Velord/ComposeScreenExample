package com.velord.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: Int,
    val name: String
)
