package com.velord.data.db.movie.dataSource

import com.velord.data.db.movie.MovieDao
import com.velord.data.db.movie.MovieEntity
import com.velord.data.db.movie.toEntity
import com.velord.model.movie.FilterType
import com.velord.model.movie.Movie
import com.velord.model.movie.MoviePagination
import com.velord.model.movie.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

private fun SortType.toSortOrder(): Int = when (this) {
    SortType.DateAscending -> 1
    SortType.DateDescending -> 0
}

@Single(binds = [MovieDbDataSource::class])
class MovieDbDataSourceImpl internal constructor(private val dao: MovieDao) : MovieDbDataSource {

    override suspend fun getPage(
        page: Int,
        sortType: SortType,
        filterRoster: List<FilterType>,
    ): List<Movie> {
        val sortOrder = sortType.toSortOrder()
        val rating: FilterType.Rating = filterRoster
            .firstOrNull { it is FilterType.Rating } as? FilterType.Rating
            ?: FilterType.Rating.DEFAULT
        val voteCount: FilterType.VoteCount = filterRoster
            .firstOrNull { it is FilterType.VoteCount } as? FilterType.VoteCount
            ?: FilterType.VoteCount.DEFAULT

        val offset = MoviePagination.calculateOffset(page)

        val movieFromDbRoster = dao.getFirstPage(
            ratingStart = rating.start,
            ratingEnd = rating.end,
            voteCountStart = voteCount.start,
            voteCountEnd = voteCount.end,
            sortOrder = sortOrder,
            orderBy = "date",
            pageSize = MoviePagination.PAGE_COUNT,
            offset = offset,
        ).map(MovieEntity::toDomain)

        return movieFromDbRoster
    }

    override suspend fun insertAll(movies: List<Movie>) {
        val entityRoster = movies
            .map(Movie::toEntity)
            .toTypedArray()
        dao.insertAll(*entityRoster)
    }

    override suspend fun update(movie: Movie) {
        dao.update(movie.toEntity())
    }

    override fun getAllLikedFlow(sortType: SortType): Flow<List<Movie>> {
        val sortOrder = sortType.toSortOrder()
        return dao.getAllLikedFlow(
            sortOrder = sortOrder,
            orderBy = "date",
        ).map { roster ->
            roster.map(MovieEntity::toDomain)
        }
    }

    override suspend fun clear() {
        dao.clear()
    }
}
