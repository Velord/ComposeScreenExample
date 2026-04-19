package com.velord.db.movie

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

@Single
class MovieDbDataSourceImpl internal constructor(
    private val store: MovieStore,
) : MovieDbDataSource {

    override suspend fun getPage(
        page: Int,
        sortType: SortType,
        filterRoster: List<FilterType>,
    ): List<Movie> {
        val sortOrder = sortType.toSortOrder()
        val rating: FilterType.Rating = filterRoster
            .firstOrNull { it is FilterType.Rating } as? FilterType.Rating
            ?: FilterType.Rating.Default
        val voteCount: FilterType.VoteCount = filterRoster
            .firstOrNull { it is FilterType.VoteCount } as? FilterType.VoteCount
            ?: FilterType.VoteCount.Default

        val offset = MoviePagination.calculateOffset(page)

        val movieFromDbRoster = store.getFirstPage(
            ratingStart = rating.start,
            ratingEnd = rating.end,
            voteCountStart = voteCount.start,
            voteCountEnd = voteCount.end,
            sortOrder = sortOrder,
            orderBy = "date",
            pageSize = MoviePagination.PAGE_COUNT,
            offset = offset,
        )

        return movieFromDbRoster.map(MovieRecord::toDomain)
    }

    override suspend fun insertAll(movies: List<Movie>) {
        store.insertAll(movies.map(Movie::toRecord))
    }

    override suspend fun update(movie: Movie) {
        store.update(movie.toRecord())
    }

    override fun getAllLikedFlow(sortType: SortType): Flow<List<Movie>> {
        val sortOrder = sortType.toSortOrder()
        return store.getAllLikedFlow(
            sortOrder = sortOrder,
            orderBy = "date",
        ).map { roster ->
            roster.map(MovieRecord::toDomain)
        }
    }

    override suspend fun clear() {
        store.clear()
    }
}
