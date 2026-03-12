package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import com.velord.usecase.movie.dataSource.MovieFavoriteDS
import com.velord.usecase.movie.dataSource.MovieSortDS
import com.velord.usecase.movie.result.GetFavoriteMovieResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class GetFavoriteMovieUCTest {

    private val calendar1 = Calendar.getInstance()
    private val calendar2 = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) } // Add a day to calendar2
    private val movie1 = Movie(1, "Movie 1", "Description 1", false, calendar1, 4.5f, 100, "imagePath1")
    private val movie2 = Movie(2, "Movie 2", "Description 2", true, calendar2, 3.8f, 50, "imagePath2")
    private val expectedMovies = listOf(movie1, movie2)

    private val favoriteDS = mockk<MovieFavoriteDS> {
        every { getFlow() } returns flow { emit(expectedMovies) }
        every { get() } returns emptyList() // Mocking to avoid unnecessary interactions
        coEvery { update(any()) } returns Unit
    }

    private val expectedOptions = listOf(
        MovieSortOption(SortType.DateAscending, false),
        MovieSortOption(SortType.DateDescending, true)
    )
    private val movieSortDS = mockk<MovieSortDS> {
        every { getSelectedFlow() } returns flowOf(expectedOptions[1]) // Default to DateDescending
        every { getFlow() } returns flow { emit(expectedOptions) }
    }

    @Test
    fun `invoke should return Success with sorted movies when favorites are available`() = runTest {
        val getFavoriteMovieUC = GetFavoriteMovieUC(favoriteDS, movieSortDS)
        val result = getFavoriteMovieUC()

        assertTrue(result is GetFavoriteMovieResult.Success)
        val movies = mutableListOf<Movie>()
        (result as GetFavoriteMovieResult.Success).flow.collect { movies.addAll(it) }
        assertEquals(expectedMovies.size, movies.size)
        assertTrue(movies[0].date.after(movies[1].date)) // Verify sorting
    }

    @Test
    fun `invoke should return Success with empty list when no favorites are available`() = runTest {
        val emptyFavoriteDS = mockk<MovieFavoriteDS> {
            every { getFlow() } returns flowOf(emptyList())
        }
        val getFavoriteMovieUC = GetFavoriteMovieUC(emptyFavoriteDS, movieSortDS)
        val result = getFavoriteMovieUC()

        assertTrue(result is GetFavoriteMovieResult.Success)
        val movies = mutableListOf<Movie>()
        (result as GetFavoriteMovieResult.Success).flow.collect { movies.addAll(it) }
        assertTrue(movies.isEmpty())
    }

    @Test
    fun `invoke should handle exceptions from favoriteDS and return MergeError`() = runTest {
        val exception = RuntimeException("Simulated error from favoriteDS")
        val errorFavoriteDS = mockk<MovieFavoriteDS> {
            every { getFlow() } throws exception
        }
        val getFavoriteMovieUC = GetFavoriteMovieUC(errorFavoriteDS, movieSortDS)
        val result = getFavoriteMovieUC()

        assertTrue(result is GetFavoriteMovieResult.MergeError)
        assertEquals(exception.message, (result as GetFavoriteMovieResult.MergeError).message)
    }

    @Test
    fun `invoke should handle exceptions during sorting and return Success with initial movies`() = runTest {
        val exception = RuntimeException("Simulated sorting exception")
        val errorFavoriteDS = mockk<MovieFavoriteDS> {
            every { getFlow() }returns flow {
                emit(expectedMovies)
                throw exception
            }
        }
        val getFavoriteMovieUC = GetFavoriteMovieUC(errorFavoriteDS, movieSortDS)
        val result = getFavoriteMovieUC()

        // Log the result object
        println("Result: $result")

        assertTrue(result is GetFavoriteMovieResult.Success)
        val movies = mutableListOf<Movie>()
        (result as GetFavoriteMovieResult.Success).flow.collect { movies.addAll(it) }

        // Log the collected movies
        println("Collected Movies:$movies")

        // Sort expectedMovies in descending order of date
        val expectedSortedMovies = expectedMovies.sortedByDescending { it.date }

        assertEquals(expectedSortedMovies, movies) // Verify the initial emission
    }

    @Test
    fun `invoke should apply sort option changes correctly`() = runTest {
        val dynamicMovieSortDS = mockk<MovieSortDS> {
            every { getSelectedFlow() } returns flow {
                emit(expectedOptions[0]) // Emit DateAscending first
                delay(50)
                emit(expectedOptions[1]) // Then emit DateDescending
            }
        }
        val getFavoriteMovieUC = GetFavoriteMovieUC(favoriteDS, dynamicMovieSortDS)
        val result = getFavoriteMovieUC()

        assertTrue(result is GetFavoriteMovieResult.Success)
        val resultFlow = (result as GetFavoriteMovieResult.Success).flow
        val emissions = mutableListOf<List<Movie>>()
        resultFlow.take(2).collect { emissions.add(it) } // Collect two emissions

        assertEquals(2, emissions.size) // Verify we got two emissions
        // Ascending (first emission) - Check IDs since dates might be very close
        assertTrue(emissions[0][0].id < emissions[0][1].id)
        // Descending (second emission) - Check IDs since dates might be very close
        assertTrue(emissions[1][0].id > emissions[1][1].id)
    }
}