package com.velord.backend.model

import com.velord.model.movie.FilterType

data class MoviePageRequest(
    val page: Int,
    val rating: FilterType.Rating,
    val voteCount: FilterType.VoteCount,
)