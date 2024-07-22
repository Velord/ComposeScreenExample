package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.usecase.movie.dataSource.MovieFavoriteDS
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class GetFavoriteMovieUCTest {

    private val movie1 = Movie(1, "Movie 1", "Description 1", false, Calendar.getInstance(), 4.5f, 100, "imagePath1")
    private val movie2 = Movie(2, "Movie2", "Description 2", true, Calendar.getInstance(),3.8f, 50, "imagePath2")
    private val expectedMovies = listOf(movie1, movie2)

    private val dataSource = mockk<MovieFavoriteDS> {
        every { getFlow() } returns flow { emit(expectedMovies) }
        // Mocking other methods to avoid unnecessary interactions
        every { get() } returns emptyList()
        coEvery { update(any()) } returns Unit
    }

    @Test
    fun `invoke should return the flow from dataSource`() = runTest {
        val getFavoriteMovieUC = GetFavoriteMovieUC(dataSource)
        val resultFlow = getFavoriteMovieUC()

        val actualMovies = resultFlow.first() // Collect the first emission from the flow

        assertEquals(expectedMovies, actualMovies) // Assert that the emitted list matches the expected list
    }

    @Test
    fun `invoke should return an empty flow when dataSource emits an empty flow`() = runTest {
        val dataSource = mockk<MovieFavoriteDS> {
            every{ getFlow() } returns emptyFlow()
            // Mocking other methods
            every { get() } returns emptyList()
            coEvery { update(any()) } returns Unit
        }

        val getFavoriteMovieUC = GetFavoriteMovieUC(dataSource)
        val resultFlow = getFavoriteMovieUC()

        val actualMovies = resultFlow.firstOrNull() // Collect the first emission or null if the flow is empty

        assertEquals(null, actualMovies) // Assert that the flow is empty
    }

    @Test
    fun `invoke should handle exceptions in dataSource getFlow and terminate`() = runTest {
        val expectedMovies1 = listOf(Movie(1, "Movie 1", "Description 1", false, Calendar.getInstance(), 4.5f, 100, "imagePath1")
        )

        val dataSource = mockk<MovieFavoriteDS> {
            every { getFlow() } returns flow {
                emit(expectedMovies1)
                throw RuntimeException("Mocked Exception") // Simulate an exception
                // No further emissions after the exception
            }
            // Mocking other methods
            every { get() } returns emptyList()
            coEvery { update(any()) } returns Unit}

        val getFavoriteMovieUC = GetFavoriteMovieUC(dataSource)
        val resultFlow = getFavoriteMovieUC()

        val actualMovies1 = resultFlow.first() // Collect the first emission before the exception
        val actualMovies2 = resultFlow.drop(1).firstOrNull() // Attempt to collect after the exception

        assertEquals(expectedMovies1, actualMovies1) // Assert the first emission
        assertEquals(null, actualMovies2) // Assert that there are no emissions after the exception
    }
}