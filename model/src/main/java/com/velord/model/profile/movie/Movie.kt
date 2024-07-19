package com.velord.model.profile.movie

import java.util.Calendar

data class Movie(
    val id: Int,
    val date: Calendar,
    val isLiked: Boolean
)