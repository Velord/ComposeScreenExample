package com.velord.gateway.movie

import android.util.Log
import com.velord.backend.ktor.MovieNetworkService
import com.velord.backend.model.MoviePageRequest
import com.velord.db.movie.MovieDbService
import com.velord.model.movie.FilterType
import com.velord.model.movie.MoviePagination
import com.velord.model.movie.MovieRosterSize
import com.velord.usecase.movie.dataSource.LoadNewPageMovieDS
import com.velord.usecase.movie.dataSource.RefreshMovieDS
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

private const val INITIAL_PAGE = 1
private const val TAG = "MoviePaginationGateway"

// Load First Page at init
// Than load from Db
// If Page is not full load form network
@Single
class MoviePaginationGateway(
    private val http: MovieNetworkService,
    private val db: MovieDbService,
    private val movieGateway: MovieGateway,
    private val movieSortGateway: MovieSortGateway
) : LoadNewPageMovieDS, RefreshMovieDS {

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d(TAG, "Error: $throwable")
    }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + errorHandler)
    private var currentPage = INITIAL_PAGE

    init {
        observe()
    }

    init {
        scope.launch {
            load()
        }
    }

    override suspend fun load(): MovieRosterSize {
        Log.d(TAG, "loadNewPage: $currentPage")
        val fromDb = loadFromDb(currentPage)
        val isPageFull = fromDb.value == MoviePagination.PAGE_COUNT
        Log.d(TAG, "loadNewPage fromDb: $fromDb")
        val newSize = if (isPageFull) {
            fromDb
        } else {
            loadFromNetwork(currentPage)
        }

        return newSize
    }

    override suspend fun refresh(): MovieRosterSize {
        movieGateway.clearInMemory()
        db.clear()

        currentPage = INITIAL_PAGE
        return load()
    }

    private suspend fun loadFromNetwork(page: Int): MovieRosterSize {
        val newPage = MoviePageRequest(
            page = page,
            rating = FilterType.Rating.Default,
            voteCount = FilterType.VoteCount.Default
        )
        val movieRoster = http.getMovie(newPage)
        val newRoster = movieRoster.results.map { it.toDomain() }
        Log.d(TAG, "loadFromNetwork newRoster: $newRoster")
        Log.d(TAG, "loadFromNetwork size: ${newRoster.size}")
        movieGateway.update { movies ->
            (movies + newRoster).toSet().toList()
        }

        db.insertAll(newRoster)
        return MovieRosterSize(newRoster.size)
    }

    private suspend fun loadFromDb(page: Int): MovieRosterSize {
        val sortType = movieSortGateway.getSelected().type
        val filterRoster = FilterType.createAll()

        val fromDb = db.getPage(
            page = page,
            sortType = sortType,
            filterRoster = filterRoster
        )
        Log.d(TAG, "loadFromDb: $fromDb")
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