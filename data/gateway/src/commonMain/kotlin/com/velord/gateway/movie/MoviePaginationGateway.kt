package com.velord.gateway.movie

import co.touchlab.kermit.Logger
import com.velord.backend.ktor.MovieNetworkDataSource
import com.velord.backend.model.MoviePageRequest
import com.velord.db.movie.MovieDbDataSource
import com.velord.model.movie.FilterType
import com.velord.model.movie.MoviePagination
import com.velord.model.movie.MovieRosterSize
import com.velord.usecase.movie.model.MovieLoadNewPageResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

private const val INITIAL_PAGE = 1
private const val TAG = "MoviePaginationGateway"
private val log = Logger.withTag(TAG)

@Single
class MoviePaginationGateway(
    private val http: MovieNetworkDataSource,
    private val db: MovieDbDataSource,
    private val movieGateway: MovieGateway,
    private val movieSortGateway: MovieSortGateway,
) {

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        log.d { "Error: $throwable" }
    }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + errorHandler)
    private var currentPage = INITIAL_PAGE

    init {
        observe()
        scope.launch {
            load()
        }
    }

    suspend fun load(): MovieLoadNewPageResult = try {
        log.d { "loadNewPage: $currentPage" }
        val fromDb = loadFromDb(currentPage)
        val isPageFull = fromDb.value == MoviePagination.PAGE_COUNT
        log.d { "loadNewPage fromDb: $fromDb" }
        val newSize = if (isPageFull) {
            fromDb
        } else {
            loadFromNetwork(currentPage)
        }

        val countOfNewItems = newSize.value
        if (countOfNewItems < MoviePagination.PAGE_COUNT) {
            MovieLoadNewPageResult.Exhausted
        } else {
            MovieLoadNewPageResult.Success
        }
    } catch (exception: Exception) {
        MovieLoadNewPageResult.LoadPageFailed(exception.message.orEmpty())
    }

    suspend fun refresh(): MovieLoadNewPageResult = try {
        movieGateway.clearInMemory()
        db.clear()

        currentPage = INITIAL_PAGE
        load()
        MovieLoadNewPageResult.Success
    } catch (exception: Exception) {
        MovieLoadNewPageResult.LoadPageFailed(exception.message.orEmpty())
    }

    private suspend fun loadFromNetwork(page: Int): MovieRosterSize {
        val newPage = MoviePageRequest(
            page = page,
            rating = FilterType.Rating.Default,
            voteCount = FilterType.VoteCount.Default,
        )
        val movieRoster = http.getMovie(newPage)
        val newRoster = movieRoster.results.map { it.toDomain() }
        log.d { "loadFromNetwork newRoster: $newRoster" }
        log.d { "loadFromNetwork size: ${newRoster.size}" }
        movieGateway.update { movies ->
            (movies + newRoster).toSet().toList()
        }

        db.insertAll(newRoster)
        return MovieRosterSize(newRoster.size)
    }

    private suspend fun loadFromDb(page: Int): MovieRosterSize {
        val sortType = movieSortGateway.getSelected().type
        val filterRoster = FilterType.ALL

        val fromDb = db.getPage(
            page = page,
            sortType = sortType,
            filterRoster = filterRoster,
        )
        log.d { "loadFromDb: $fromDb" }
        movieGateway.update { movies ->
            (movies + fromDb).toSet().toList()
        }
        return MovieRosterSize(fromDb.size)
    }

    private fun observe() {
        scope.launch {
            movieGateway.getFlow().collect {
                currentPage = MoviePagination.calculatePage(it.size)
            }
        }
    }
}
