package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.usecase.movie.result.GetMovieResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavoriteMovieUC(private val useCase: GetAllMovieUC) {

    suspend operator fun invoke(): GetMovieResult = when(val result = useCase()) {
        is GetMovieResult.Success -> result.copy(flow = result.flow.filter())
        is GetMovieResult.DBError -> result.copy(flow = result.flow.filter())
    }

    private fun Flow<List<Movie>>.filter(): Flow<List<Movie>> =
        map { roster -> roster.filter { it.isLiked } }
}