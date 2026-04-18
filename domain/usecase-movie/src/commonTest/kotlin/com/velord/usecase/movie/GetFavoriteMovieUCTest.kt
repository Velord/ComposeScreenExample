package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock

class GetFavoriteMovieUCTest {

    private val date1 = Clock.System.now()
    private val date2 = date1.plus(1, DateTimeUnit.DAY, TimeZone.UTC)
    private val movie1 = Movie(1, "Movie 1", "Description 1", false, date1, 4.5f, 100, "imagePath1")
    private val movie2 = Movie(2, "Movie 2", "Description 2", true, date2, 3.8f, 50, "imagePath2")
    private val expectedMovies = listOf(movie1, movie2)

    private val favoriteDS = mock<MovieFavoriteDS> {
        every { getFlow() } returns flow { emit(expectedMovies) }
        every { get() } returns emptyList()
        everySuspend { update(any()) } returns Unit
    }

    private val expectedOptions = listOf(
        MovieSortOption(SortType.DateAscending, false),
        MovieSortOption(SortType.DateDescending, true)
    )
    private val movieSortDS = mock<MovieSortDS> {
        every { getSelectedFlow() } returns flowOf(expectedOptions[1])
        every { getFlow() } returns flow { emit(expectedOptions) }
    }

    @Test
    fun `invoke should return Success with sorted movies when favorites are available`() = runTest {
        val getFavoriteMovieUC = GetFavoriteMovieUCImpl(favoriteDS, movieSortDS)
        val result = getFavoriteMovieUC()

        assertTrue(result is GetFavoriteMovieResult.Success)
        val movies = mutableListOf<Movie>()
        (result as GetFavoriteMovieResult.Success).flow.collect { movies.addAll(it) }
        assertEquals(expectedMovies.size, movies.size)
        assertTrue(movies[0].date > movies[1].date)
    }

    @Test
    fun `invoke should return Success with empty list when no favorites are available`() = runTest {
        val emptyFavoriteDS = mock<MovieFavoriteDS> {
            every { getFlow() } returns flowOf(emptyList())
        }
        val getFavoriteMovieUC = GetFavoriteMovieUCImpl(emptyFavoriteDS, movieSortDS)
        val result = getFavoriteMovieUC()

        assertTrue(result is GetFavoriteMovieResult.Success)
        val movies = mutableListOf<Movie>()
        (result as GetFavoriteMovieResult.Success).flow.collect { movies.addAll(it) }
        assertTrue(movies.isEmpty())
    }

    @Test
    fun `invoke should handle exceptions from favoriteDS and return MergeError`() = runTest {
        val exception = RuntimeException("Simulated error from favoriteDS")
        val errorFavoriteDS = mock<MovieFavoriteDS> {
            every { getFlow() } throws exception
        }
        val getFavoriteMovieUC = GetFavoriteMovieUCImpl(errorFavoriteDS, movieSortDS)
        val result = getFavoriteMovieUC()

        assertTrue(result is GetFavoriteMovieResult.MergeError)
        assertEquals(exception.message, (result as GetFavoriteMovieResult.MergeError).message)
    }

    @Test
    fun `invoke should handle exceptions during sorting and return Success with initial movies`() = runTest {
        val exception = RuntimeException("Simulated sorting exception")
        val errorFavoriteDS = mock<MovieFavoriteDS> {
            every { getFlow() } returns flow {
                emit(expectedMovies)
                throw exception
            }
        }
        val getFavoriteMovieUC = GetFavoriteMovieUCImpl(errorFavoriteDS, movieSortDS)
        val result = getFavoriteMovieUC()

        println("Result: $result")

        assertTrue(result is GetFavoriteMovieResult.Success)
        val movies = mutableListOf<Movie>()
        (result as GetFavoriteMovieResult.Success).flow.collect { movies.addAll(it) }

        println("Collected Movies:$movies")

        val expectedSortedMovies = expectedMovies.sortedByDescending { it.date }
        assertEquals(expectedSortedMovies, movies)
    }

    @Test
    fun `invoke should apply sort option changes correctly`() = runTest {
        val dynamicMovieSortDS = mock<MovieSortDS> {
            every { getSelectedFlow() } returns flow {
                emit(expectedOptions[0])
                delay(50)
                emit(expectedOptions[1])
            }
        }
        val getFavoriteMovieUC = GetFavoriteMovieUCImpl(favoriteDS, dynamicMovieSortDS)
        val result = getFavoriteMovieUC()

        assertTrue(result is GetFavoriteMovieResult.Success)
        val resultFlow = (result as GetFavoriteMovieResult.Success).flow
        val emissions = mutableListOf<List<Movie>>()
        resultFlow.take(2).collect { emissions.add(it) }

        assertEquals(2, emissions.size)
        assertTrue(emissions[0][0].id < emissions[0][1].id)
        assertTrue(emissions[1][0].id > emissions[1][1].id)
    }
}
