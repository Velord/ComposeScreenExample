package com.velord.db.movie

import com.velord.model.movie.FilterType
import com.velord.model.movie.Movie
import com.velord.model.movie.SortType
import kotlinx.coroutines.flow.Flow

interface MovieDbDataSource {
    suspend fun getPage(
        page: Int,
        sortType: SortType,
        filterRoster: List<FilterType>,
    ): List<Movie>

    suspend fun insertAll(movies: List<Movie>)
    suspend fun update(movie: Movie)
    fun getAllLikedFlow(sortType: SortType): Flow<List<Movie>>
    suspend fun clear()
}
