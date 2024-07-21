package com.velord.gateway.movie

import android.util.Log
import com.velord.appstate.AppStateService
import com.velord.backend.ktor.MovieService
import com.velord.backend.model.MoviePageRequest
import com.velord.db.MovieDbService
import com.velord.model.movie.FilterType
import com.velord.model.movie.Movie
import com.velord.model.movie.SortType
import com.velord.usecase.movie.dataSource.MovieDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

private const val INITIAL_PAGE = 1

@Single
class MovieGateway(
    private val appState: AppStateService,
    private val http: MovieService,
    private val db: MovieDbService,
) : MovieDS {

    private var currentPage: Int = INITIAL_PAGE

    override fun getFlow(): Flow<List<Movie>> = appState.movieRosterFlow

    override fun get(): List<Movie> = appState.movieRosterFlow.value

    override fun update(movie: Movie) {
        appState.movieRosterFlow.update { roster ->
            roster.map {
                if (it.id == movie.id) {
                    movie
                } else {
                    it
                }
            }
        }
    }

    override suspend fun loadNewPage(): Int {
        val movieRoster = http.getMovie(MoviePageRequest(currentPage++))
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

    override suspend fun loadFromDB() {
        val sortType = appState.movieSortFlow.value
            .firstOrNull { it.isSelected }
            ?.type
            ?: SortType.DateDescending
        val filterRoster = FilterType.createAll()

        val fromDb = db.getFirstPage(
            sortType = sortType,
            filterRoster = filterRoster
        )
        Log.d("@@@", "fromDb: $fromDb")
        appState.movieRosterFlow.value = fromDb
    }
}