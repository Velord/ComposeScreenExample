package com.velord.db

import android.util.Log
import com.velord.model.movie.FilterType
import com.velord.model.movie.Movie
import com.velord.model.movie.MoviePagination
import com.velord.model.movie.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

private fun SortType.toSortOrder(): Int = when (this) {
    SortType.DateAscending -> 1
    SortType.DateDescending -> 0
}

interface MovieDbService {
    suspend fun getPage(
        page: Int,
        sortType: SortType,
        filterRoster: List<FilterType>
    ): List<Movie>
    suspend fun insertAll(movies: List<Movie>)
    suspend fun update(movie: Movie)
    fun getAllLikedFlow(): Flow<List<Movie>>
}

@Single
class MovieDbServiceImpl(
    @Provided private val db: MovieDao
) : MovieDbService {

    override suspend fun getPage(
        page: Int,
        sortType: SortType,
        filterRoster: List<FilterType>
    ): List<Movie> {
        val sortOrder = sortType.toSortOrder()
        val rating: FilterType.Rating = filterRoster
            .firstOrNull { it is FilterType.Rating } as? FilterType.Rating
            ?: FilterType.Rating.Default
        val voteCount: FilterType.VoteCount = filterRoster
            .firstOrNull { it is FilterType.VoteCount } as? FilterType.VoteCount
            ?: FilterType.VoteCount.Default
        val offset = page * MoviePagination.PAGE_COUNT
        Log.d("@@@", "sortOrder: $sortOrder, rating: $rating, voteCount: $voteCount, offset: $offset")
        val movieFromDbRoster = db.getFirstPage(
            ratingStart = rating.start,
            ratingEnd = rating.end,
            voteCountStart = voteCount.start,
            voteCountEnd = voteCount.end,
            sortOrder = sortOrder,
            orderBy = "date",
            pageSize = MoviePagination.PAGE_COUNT,
            offset = offset
        )

        return movieFromDbRoster.map { it.toDomain() }
    }

    override suspend fun insertAll(movies: List<Movie>) {
        db.insertAll(*movies.map { MovieEntity(it) }.toTypedArray())
    }

    override suspend fun update(movie: Movie) {
        db.update(MovieEntity(movie))
    }

    override fun getAllLikedFlow(): Flow<List<Movie>> =
        db.getAllLikedFlow().map { it.map { entity -> entity.toDomain() } }
}