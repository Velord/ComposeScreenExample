package com.velord.data.backend.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieRosterResponse(
    val page: Int,
    val results: List<MovieResponse>,
)
