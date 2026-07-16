package com.velord.data.backend.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieRosterResponse(
    val page: Int,
    @SerialName("results")
    val roster: List<MovieResponse>,
)
