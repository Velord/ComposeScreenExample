package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.model.movie.MovieSortOption
import com.velord.model.movie.SortType
import com.velord.usecase.movie.dataSource.MovieDS
import com.velord.usecase.movie.dataSource.MovieSortDS
import com.velord.usecase.movie.result.GetMovieResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.util.Calendar

class GetAllMovieUCTest {

    private val Descending = MovieSortOption(SortType.DateDescending, true)
    private val Ascending = MovieSortOption(SortType.DateAscending, true)

    private val movie1 = Movie(1, "Movie 1", "Description 1", false, Calendar.getInstance(), 4.5f, 100, "imagePath1")
    private val movie2 = Movie(2, "Movie 2", "Description 2", true, Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }, 3.8f, 50, "imagePath2")
    private val movieList = listOf(movie1, movie2)

    private val movieDS = mockk<MovieDS> {
        coEvery { get() } returns movieList
        coEvery { loadFromDb() } returns 0
        every { getFlow() } returns flowOf(movieList)
    }
    private val movieSortDS = mockk<MovieSortDS> {
        every { getSelectedFlow() } returns flowOf(Descending)
    }

    @Test
    fun `invoke should return Success when movies are available and sorted by DateDescending`() = runTest {
        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val result = getAllMovieUC()

        assertTrue(result is GetMovieResult.Success)
        val movies = (result as GetMovieResult.Success).flow.toList().flatten() // Get the list of movies from the flow
        assertEquals(2, movies.size)
        assertTrue(movies[0].date.after(movies[1].date)) // Verify descending date order
    }

    @Test
    fun `invoke should return Success when movies are available and sorted by DateAscending`() = runTest {
        // ... (Similar to the previous test, but with SortType.DateAscending)
        val movieSortDS = mockk<MovieSortDS> {
            every { getSelectedFlow() } returns flowOf(Ascending)
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val result = getAllMovieUC()

        assertTrue(result is GetMovieResult.Success)
        val movies = (result as GetMovieResult.Success).flow.toList().flatten()
        assertEquals(2, movies.size)
        assertTrue(movies[0].date.before(movies[1].date)) // Verify ascending date order
    }

    @Test
    fun `invoke should return DBError when an exception occurs`() = runTest {
        val movieDS = mockk<MovieDS> {
            coEvery { get() } returns emptyList()
            coEvery { loadFromDb() } throws Exception("Database error")
            every { getFlow() } returns flowOf(emptyList())
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val result = getAllMovieUC()

        assertTrue(result is GetMovieResult.DBError)
        assertEquals("Database error", (result as GetMovieResult.DBError).message)
        assertTrue(result.flow.toList().flatten().isEmpty())
    }

    @Test
    fun `invoke should load movies from DB when initially empty`() = runTest {
        val movieDS = mockk<MovieDS> {
            coEvery { get() } returns emptyList()
            coEvery { loadFromDb() } returns 0
            every { getFlow() } returns flowOf(listOf(movie1))
        }
        val movieSortDS = mockk<MovieSortDS> {
            every { getSelectedFlow() } returns flowOf(Descending)
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        getAllMovieUC() // Call invoke to trigger loading from DB

        coVerify { movieDS.loadFromDb() } // Verify that loadFromDB was called
    }

    @Test
    fun `invoke should handle empty movie list`() = runTest {
        val movieDS = mockk<MovieDS> {
            coEvery { get() } returns emptyList()
            coEvery { loadFromDb() } returns 0
            every { getFlow() } returns flowOf(emptyList())
            coEvery { loadNewPage() } returns 0
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val result = getAllMovieUC()

        assertTrue(result is GetMovieResult.Success) // Success is still expected
        val movies = (result as GetMovieResult.Success).flow.toList().flatten()
        assertTrue(movies.isEmpty()) // Verify that the resulting list is empty
    }

    @Test
    fun `invoke should handle movies with same date when sorting`() = runTest {
        val sameDate = Calendar.getInstance()
        val movieDS = mockk<MovieDS> {
            coEvery { get() } returns listOf(
                Movie(1, "Movie 1", "Description 1", false, sameDate, 4.5f, 100),
                Movie(2, "Movie 2", "Description 2", true, sameDate, 3.8f, 50)
            )
            coEvery { loadFromDb() } returns 0
            every { getFlow() } returns flowOf(listOf(
                Movie(1, "Movie 1", "Description 1", false, sameDate, 4.5f, 100),
                Movie(2, "Movie 2", "Description 2", true, sameDate, 3.8f, 50)
            ))
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val result = getAllMovieUC()

        assertTrue(result is GetMovieResult.Success)
        val movies = (result as GetMovieResult.Success).flow.toList().flatten()
        assertEquals(2, movies.size)
        // You might want to add assertions based on how you expect movies with the same date to be ordered
    }

    @Test
    fun `invoke should handle exception during sorting and return MergeError (mocking private function)`() = runTest {
        val exception = RuntimeException("Simulated sorting exception")
        val getAllMovieUC = spyk(GetAllMovieUC(mockk(), mockk())) // Create a spy object

        // Mock the private mergeMovieWithSort function to throw an exception
        every { getAllMovieUC["mergeMovieWithSort"]() } throws exception

        val result = getAllMovieUC()

        assertTrue(result is GetMovieResult.MergeError)
        assertEquals(exception.message, (result as GetMovieResult.MergeError).message)
    }

    @Test
    fun `invoke should handle extremely large movie list efficiently`() = runTest {
        val largeMovieList = (1..100000).map {
            Movie(it, "Movie $it", "Desc $it", false, Calendar.getInstance(), (it % 5).toFloat(), it * 10)
        }
        val movieDS = mockk<MovieDS> {
            coEvery { get() } returns largeMovieList
            coEvery { loadFromDb() } returns 0
            every { getFlow() } returns flowOf(largeMovieList)
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)

        // Measure the time taken to get the result
        val startTime = System.currentTimeMillis()
        val result = getAllMovieUC()
        val endTime = System.currentTimeMillis()

        assertTrue(result is GetMovieResult.Success)
        // Add an assertion to check if the sorting was performed within an acceptable time limit
        assertTrue(endTime - startTime < 500) // Adjust the time limit as needed
    }

    @Test
    fun `invoke should handle errors emitted by upstream flows and return Success with empty flow`() = runTest {
        val exception = RuntimeException("Simulated error from movieDS")
        val movieDS = mockk<MovieDS> {
            every { getFlow() } returns flow {
                emit(emptyList())
                throw exception
            }
            coEvery { get() } returns emptyList()
            coEvery { loadFromDb() } returns 0
            coEvery { loadNewPage() } returns 0 // Mock loadNewPage()
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val result = getAllMovieUC()

        assertTrue(result is GetMovieResult.Success)
        val movies = (result as GetMovieResult.Success).flow.first()
        assertTrue(movies.isEmpty())
        coVerify { movieDS.loadNewPage() } // Verify that loadNewPage() was called
    }

    @Test
    fun `invoke should handle sort option changes and apply them correctly`() = runTest {
        val movieSortDS = mockk<MovieSortDS> {
            every { getSelectedFlow() } returns flow {
                emit(Descending)
                delay(50)
                emit(Ascending)
            }
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val result = getAllMovieUC()

        assertTrue(result is GetMovieResult.Success) // Assert Success
        val resultFlow = (result as GetMovieResult.Success).flow // Extract the flow

        val firstEmission = withTimeoutOrNull(100) { resultFlow.first() }
        val secondEmission = withTimeoutOrNull(100) { resultFlow.drop(1).first() }

        assertNotNull(firstEmission)
        assertNotNull(secondEmission)
        // Use safe calls to handle potential nulls in the lists
        assertTrue(firstEmission?.get(0)?.date?.after(firstEmission[1].date) ?: false) // Descending
        assertTrue(secondEmission?.get(0)?.date?.before(secondEmission[1].date) ?: false) // Ascending
    }

    @Test
    fun `invoke should handle DBError when exception occurs during loadFromDB and flow is not empty`() = runTest {
        val exception = RuntimeException("Database error")
        val movieDS = mockk<MovieDS> {
            coEvery { get() } returns emptyList()
            coEvery { loadFromDb() } throws exception
            every { getFlow() } returns flowOf(listOf(movie1)) // Non-empty flow
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val result = getAllMovieUC()

        assertTrue(result is GetMovieResult.DBError)
        assertEquals("Database error", (result as GetMovieResult.DBError).message)// Use safe call to handle potential null in the list
        assertEquals(1, result.flow.firstOrNull()?.size ?: 0)
    }

    @Test
    fun `invoke should handle cancellation gracefully and not emit results after cancellation`() = runTest {
        val movieDS = mockk<MovieDS> {
            every { getFlow() } returns flow {
                delay(500) // Simulate a long-running operation
                emit(listOf(movie1))
            }
            coEvery { loadFromDb() } returns 0
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val collectedMovies = mutableListOf<Movie>()
        val job = launch {
            val result = getAllMovieUC()
            if (result is GetMovieResult.Success) {
                result.flow.collect { movies ->
                    collectedMovies.addAll(movies)
                }
            }
        }

        delay(100) // Allow some time for the flow to start
        job.cancel() // Cancel the coroutine
        delay(600) // Wait longer than the simulated delay in the flow

        // Assert that no movies were collected after cancellation
        assertTrue(collectedMovies.isEmpty())
    }

    @Test
    fun `invoke should handle multiple emissions from movieDS getFlow`() = runTest {
        val movieDS = mockk<MovieDS> {
            every { getFlow() } returns flow {
                emit(listOf(movie1))
                delay(50)
                emit(listOf(movie1, movie2))
            }
            coEvery { get() } returns listOf(movie1)
            coEvery { loadFromDb() } returns 0
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val result = getAllMovieUC()

        assertTrue(result is GetMovieResult.Success)
        val resultFlow = (result as GetMovieResult.Success).flow

        val firstEmission = withTimeoutOrNull(100) { resultFlow.first() }
        val secondEmission = withTimeoutOrNull(100) { resultFlow.drop(1).first() }

        assertNotNull(firstEmission)
        assertNotNull(secondEmission)

        assertEquals(1, firstEmission?.size) // First emission should have one movie
        assertEquals(2, secondEmission?.size) // Second emission should have two movies
    }

    @Test
    fun `invoke should handle very late emission from movieSortDS getSelectedFlow`() = runTest {
        val movieSortDS = mockk<MovieSortDS> {
            every { getSelectedFlow() } returns flow {
                delay(500) // Simulate a very late emission
                emit(Descending)
            }
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val result = withTimeoutOrNull(1000) { getAllMovieUC() }

        assertNotNull(result) // Assert that the result is null (timeout)
        assertTrue(result is GetMovieResult.Success) // Assert that the result is Success
    }

    @Test
    fun `invoke should handle concurrent emissions from both movieDS and movieSortDS`() = runTest {
        val movie3 = Movie(3, "Movie 3", "Desc 3", false, Calendar.getInstance(), 4.2f, 80, "imagePath3")
        val movie4 = Movie(4, "Movie 4", "Desc 4", true, Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }, 3.5f, 60, "imagePath4")
        val movieDS = mockk<MovieDS> {
            every { getFlow() } returns flow {
                emit(listOf(movie3))
                delay(50)
                emit(listOf(movie3, movie4))
            }
            coEvery { get() } returns listOf(movie1)
            coEvery { loadFromDb() } returns 0
        }
        val movieSortDS = mockk<MovieSortDS> {
            every { getSelectedFlow() } returns flow {
                delay(25) // Emit a sort option slightly after the first movie emission
                emit(Descending)
                delay(75) // Emit another sort option after the second movie emission
                emit(Ascending)
            }
        }

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val result = getAllMovieUC()

        assertTrue(result is GetMovieResult.Success)
        val resultFlow = (result as GetMovieResult.Success).flow

        val firstEmission = withTimeoutOrNull(100) { resultFlow.first() }
        val secondEmission = withTimeoutOrNull(150) { resultFlow.drop(1).first() }

        // Handle potential nulls from withTimeoutOrNull
        if (firstEmission == null || secondEmission == null) {
            fail("Emissions should not be null within the given timeouts")
        } else {
            // Assertions for non-null emissions
            assertEquals(1, firstEmission.size)
            assertEquals(2, secondEmission.size)
            assertTrue(secondEmission[0].date.after(secondEmission[1].date))
        }
    }

    @Test
    fun `invoke should handle exceptions in movieDS getFlow and return a MergeError`() = runTest {
        val movieDS = mockk<MovieDS> {
            every { getFlow() } throws RuntimeException("Mocked Exception") // Simulate an exception
        }
        val movieSortDS = mockk<MovieSortDS>() // We don't need to mock this for this test

        val getAllMovieUC = GetAllMovieUC(movieDS, movieSortDS)
        val result = getAllMovieUC()

        assertTrue(result is GetMovieResult.MergeError) // Assert that the result is a MergeError
    }

    @Test
    fun `invoke should handle movies with same date in both sort orders`() = runTest {
        val sameDate = Calendar.getInstance()
        val movie3 = Movie(3, "Movie 3", "Desc 3", false, sameDate, 4.2f,80, "imagePath3")
        val movie4 = Movie(4, "Movie 4", "Desc 4", true, sameDate, 3.5f, 60, "imagePath4")
        val movieListWithSameDate = listOf(movie3, movie4)

        val movieDS = mockk<MovieDS> {
            coEvery { get() } returns movieListWithSameDate
            coEvery { loadFromDb() } returns 0
            every { getFlow() } returns flowOf(movieListWithSameDate)
        }

        //Test with Descending order
        val movieSortDSDescending = mockk<MovieSortDS> {
            every { getSelectedFlow() } returns flowOf(Descending)
        }
        val getAllMovieUCDescending = GetAllMovieUC(movieDS, movieSortDSDescending)
        val resultDescending = getAllMovieUCDescending()
        assertTrue(resultDescending is GetMovieResult.Success)
        val moviesDescending = (resultDescending as GetMovieResult.Success).flow.first()
        assertEquals(movie3, moviesDescending[0]) // Order might be based on insertion order or another property
        assertEquals(movie4, moviesDescending[1])

        // Test with Ascending order
        val movieSortDSAscending = mockk<MovieSortDS> {
            every { getSelectedFlow() } returns flowOf(Ascending)
        }
        val getAllMovieUCAscending = GetAllMovieUC(movieDS, movieSortDSAscending)
        val resultAscending = getAllMovieUCAscending()
        assertTrue(resultAscending is GetMovieResult.Success)
        val moviesAscending = (resultAscending as GetMovieResult.Success).flow.first()
        assertEquals(movie3, moviesAscending[0]) // Assumingyou want to order by ID in ascending order
        assertEquals(movie4, moviesAscending[1])
    }
}