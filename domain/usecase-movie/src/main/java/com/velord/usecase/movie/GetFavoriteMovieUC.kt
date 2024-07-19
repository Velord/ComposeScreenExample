package com.velord.usecase.movie

import com.velord.model.movie.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavoriteMovieUC(
    private val useCase: GetAllMovieUC
) {
    suspend operator fun invoke(): Flow<List<Movie>> = useCase().map { roster ->
        roster.filter { it.isLiked }
    }
}