package com.velord.db.movie

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single(binds = [MovieStore::class])
class RoomMovieStore(
    @Provided private val dao: MovieDao,
) : MovieStore {

    override suspend fun getFirstPage(
        ratingStart: Float,
        ratingEnd: Float,
        voteCountStart: Int,
        voteCountEnd: Int,
        orderBy: String,
        sortOrder: Int,
        pageSize: Int,
        offset: Int,
    ): List<MovieRecord> = dao.getFirstPage(
        ratingStart = ratingStart,
        ratingEnd = ratingEnd,
        voteCountStart = voteCountStart,
        voteCountEnd = voteCountEnd,
        orderBy = orderBy,
        sortOrder = sortOrder,
        pageSize = pageSize,
        offset = offset,
    ).map(MovieEntity::toRecord)

    override suspend fun insertAll(movies: List<MovieRecord>) {
        dao.insertAll(*movies.map(MovieEntity::fromRecord).toTypedArray())
    }

    override suspend fun update(movie: MovieRecord) {
        dao.update(MovieEntity.fromRecord(movie))
    }

    override fun getAllLikedFlow(
        orderBy: String,
        sortOrder: Int,
    ): Flow<List<MovieRecord>> = dao.getAllLikedFlow(
        orderBy = orderBy,
        sortOrder = sortOrder,
    ).map { roster ->
        roster.map(MovieEntity::toRecord)
    }

    override suspend fun clear() {
        dao.clear()
    }
}
