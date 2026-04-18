package com.velord.usecase.movie

import com.velord.model.movie.Movie

fun interface UpdateMovieLikeUC : suspend (Movie) -> Unit
