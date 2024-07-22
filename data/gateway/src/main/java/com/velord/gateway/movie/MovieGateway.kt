package com.velord.gateway.movie

import android.util.Log
import com.velord.appstate.AppStateService
import com.velord.backend.ktor.MovieService
import com.velord.backend.model.MoviePageRequest
import com.velord.db.MovieDbService
import com.velord.model.movie.FilterType
import com.velord.model.movie.Movie
import com.velord.model.movie.MoviePagination
import com.velord.usecase.movie.dataSource.MovieDS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

private const val INITIAL_PAGE = 1

@Single
class MovieGateway(
    private val appState: AppStateService,
    private val http: MovieService,
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

    override suspend fun loadNewPage(): Int {
        val newPage = MoviePageRequest(
            page = currentPage++,
            rating = FilterType.Rating.Default,
            voteCount = FilterType.VoteCount.Default
        )
        val movieRoster = http.getMovie(newPage)
        val newRoster = movieRoster.results.map { it.toDomain() }
        appState.movieRosterFlow.update { movies ->
            (movies + newRoster).toSet().toList()
        }

        db.insertAll(newRoster)

        return newRoster.size
    }

    override suspend fun refresh(): Int {
        appState.movieRosterFlow.value = emptyList()

        currentPage = INITIAL_PAGE
        return loadNewPage()
    }

    override suspend fun loadFromDb(): Int {
        val sortType = movieSortGateway.getSelected().type
        val filterRoster = FilterType.createAll()

        val fromDb = db.getPage(
            page = INITIAL_PAGE,
            sortType = sortType,
            filterRoster = filterRoster
        )
        Log.d("@@@", "fromDb: $fromDb")
        appState.movieRosterFlow.value = fromDb
        return fromDb.size
    }

    private fun observe() {
        scope.launch {
            appState.movieRosterFlow.collect {
                currentPage = MoviePagination.calculatePage(it.size)
            }
        }
    }
}