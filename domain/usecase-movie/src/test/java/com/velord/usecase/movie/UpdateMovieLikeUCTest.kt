package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.usecase.movie.dataSource.MovieFavoriteDS
import com.velord.usecase.movie.result.UpdateMovieResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class UpdateMovieLikeUCTest {

    private val movie = Movie(
        1, "Movie Title", "Description", false, Calendar.getInstance(),
        4.5f, 100, "image.jpg"
    )

    @Test
    fun `invoke should toggle isLiked and update dataSource`() = runTest {
        val dataSource = mockk<MovieFavoriteDS>(relaxed = true)
        val useCase = UpdateMovieLikeUC(dataSource)
        val result = useCase(movie)

        assertEquals(UpdateMovieResult.Success, result)
        coVerify { dataSource.update(movie.copy(isLiked = true)) }
    }

    @Test
    fun `invoke should handle toggling isLiked from true to false`() = runTest {
        val dataSource = mockk<MovieFavoriteDS>(relaxed = true)
        val useCase = UpdateMovieLikeUC(dataSource)
        val result = useCase(movie.copy(isLiked = true))

        assertEquals(UpdateMovieResult.Success, result)
        coVerify { dataSource.update(movie.copy(isLiked = false)) }
    }

    @Test
    fun `invoke should return DbError when dataSource throws an exception`() = runTest {
        val errorMessage = "Database Error"
        val dataSource = mockk<MovieFavoriteDS> {
            coEvery { update(any()) } throws Exception(errorMessage)
        }
        val useCase = UpdateMovieLikeUC(dataSource)
        val result = useCase(movie)

        assertEquals(UpdateMovieResult.DbError(errorMessage), result)
    }

    @Test
    fun `invoke should return DbError with empty message when exception has no message`() = runTest {
        val dataSource = mockk<MovieFavoriteDS> {
            coEvery { update(any()) } throws Exception()
        }
        val useCase = UpdateMovieLikeUC(dataSource)
        val result = useCase(movie)

        assertEquals(UpdateMovieResult.DbError(""), result)
    }

    @Test
    fun `invoke should update dataSource with the correct movie ID`() = runTest {
        val dataSource = mockk<MovieFavoriteDS>(relaxed = true)
        val useCase = UpdateMovieLikeUC(dataSource)
        useCase(movie)

        coVerify { dataSource.update(movie.copy(isLiked = true)) }
    }


    @Test
    fun `invoke should handle consecutive toggles on the same movie`() = runTest {
        val dataSource = mockk<MovieFavoriteDS>(relaxed = true)
        val useCase = UpdateMovieLikeUC(dataSource)

        useCase(movie) // Toggle once
        val result = useCase(movie.copy(isLiked = true))// Toggle again, starting with isLiked = true

        assertEquals(UpdateMovieResult.Success, result)
        coVerify(exactly = 2) { dataSource.update(any()) } // Verify update is called twice
        coVerify { dataSource.update(movie.copy(isLiked = false)) } // Verify the final state is isLiked = false
    }

    @Test
    fun `invoke should handle movies with same ID but different other properties`() = runTest {
        val dataSource = mockk<MovieFavoriteDS>(relaxed = true)
        val useCase = UpdateMovieLikeUC(dataSource)
        val movie2 = movie.copy(title = "Different Title", rating = 3.0f) // Same ID, different properties

        useCase(movie)
        val result = useCase(movie2)

        assertEquals(UpdateMovieResult.Success, result)
        coVerify {
            dataSource.update(movie.copy(isLiked = true))
            dataSource.update(movie2.copy(isLiked = true)) // Verify both movies are updated independently
        }
    }
}