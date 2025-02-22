package com.velord.gateway.movie

import android.util.Log
import com.velord.appstate.AppStateService
import com.velord.backend.ktor.NetworkMovieService
import com.velord.backend.model.MoviePageRequest
import com.velord.db.movie.MovieDbService
import com.velord.model.movie.FilterType
import com.velord.model.movie.Movie
import com.velord.model.movie.MoviePagination
import com.velord.model.movie.MovieRosterSize
import com.velord.usecase.movie.dataSource.MovieDS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

private const val INITIAL_PAGE = 1

// Load First Page at init
// Than load from Db if Page is not full load form network
@Single
class MovieGateway(
    private val appState: AppStateService,
    private val http: NetworkMovieService,
    private val db: MovieDbService,
    private val movieSortGateway: MovieSortGateway
) : MovieDS {

    private val scope = CoroutineScope(Dispatchers.IO)
    private var currentPage = INITIAL_PAGE

    init {
        observe()
    }

    override fun getFlow(): Flow<List<Movie>> = appState.movieRosterFlow

    override fun get(): List<Movie> = appState.movieRosterFlow.value

    init {
        scope.launch {
            loadNewPage()
        }
    }

    override suspend fun loadNewPage(): MovieRosterSize {
        Log.d("MovieGateway", "loadNewPage: $currentPage")
        val fromDb = loadFromDb(currentPage)
        val isPageFull = fromDb.value == MoviePagination.PAGE_COUNT
        Log.d("MovieGateway", "loadNewPage fromDb: $fromDb")
        val newSize = if (isPageFull) {
            fromDb
        } else {
            loadFromNetwork(currentPage)
        }

        return newSize
    }

    override suspend fun refresh(): MovieRosterSize {
        appState.movieRosterFlow.value = emptyList()
        db.clear()

        currentPage = INITIAL_PAGE
        return loadNewPage()
    }

    private suspend fun loadFromNetwork(page: Int): MovieRosterSize {
        val newPage = MoviePageRequest(
            page = page,
            rating = FilterType.Rating.Default,
            voteCount = FilterType.VoteCount.Default
        )
        val movieRoster = http.getMovie(newPage)
        val newRoster = movieRoster.results.map { it.toDomain() }
        Log.d("MovieGateway", "loadFromNetwork newRoster: $newRoster")
        Log.d("MovieGateway", "loadFromNetwork size: ${newRoster.size}")
        appState.movieRosterFlow.update { movies ->
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
        Log.d("MovieGateway", "loadFromDb: $fromDb")
        appState.movieRosterFlow.update { movies ->
            (movies + fromDb).toSet().toList()
        }
        return MovieRosterSize(fromDb.size)
    }

    private fun observe() {
        scope.launch {
            appState.movieRosterFlow.collect {
                currentPage = MoviePagination.calculatePage(it.size)
            }
        }
    }
}