package com.velord.usecase.movie

import com.velord.model.movie.Movie
import com.velord.usecase.movie.model.UpdateMovieResult
import dev.mokkery.MockMode
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock

class UpdateMovieLikeUCTest {

    private val movie = Movie(
        1, "Movie Title", "Description", false, Clock.System.now(),
        4.5f, 100, "image.jpg"
    )

    @Test
    fun `invoke should toggle isLiked and update dataSource`() = runTest {
        val dataSource = mock<MovieFavoriteDS>(MockMode.autoUnit)
        val useCase = UpdateMovieLikeUCImpl(dataSource)
        val result = useCase(movie)

        assertEquals(UpdateMovieResult.Success, result)
        verifySuspend { dataSource.update(movie.copy(isLiked = true)) }
    }

    @Test
    fun `invoke should handle toggling isLiked from true to false`() = runTest {
        val dataSource = mock<MovieFavoriteDS>(MockMode.autoUnit)
        val useCase = UpdateMovieLikeUCImpl(dataSource)
        val result = useCase(movie.copy(isLiked = true))

        assertEquals(UpdateMovieResult.Success, result)
        verifySuspend { dataSource.update(movie.copy(isLiked = false)) }
    }

    @Test
    fun `invoke should return DbError when dataSource throws an exception`() = runTest {
        val errorMessage = "Database Error"
        val dataSource = mock<MovieFavoriteDS> {
            everySuspend { update(any()) } throws Exception(errorMessage)
        }
        val useCase = UpdateMovieLikeUCImpl(dataSource)
        val result = useCase(movie)

        assertEquals(UpdateMovieResult.DbError(errorMessage), result)
    }

    @Test
    fun `invoke should return DbError with empty message when exception has no message`() = runTest {
        val dataSource = mock<MovieFavoriteDS> {
            everySuspend { update(any()) } throws Exception()
        }
        val useCase = UpdateMovieLikeUCImpl(dataSource)
        val result = useCase(movie)

        assertEquals(UpdateMovieResult.DbError(""), result)
    }

    @Test
    fun `invoke should update dataSource with the correct movie ID`() = runTest {
        val dataSource = mock<MovieFavoriteDS>(MockMode.autoUnit)
        val useCase = UpdateMovieLikeUCImpl(dataSource)
        useCase(movie)

        verifySuspend { dataSource.update(movie.copy(isLiked = true)) }
    }

    @Test
    fun `invoke should handle consecutive toggles on the same movie`() = runTest {
        val dataSource = mock<MovieFavoriteDS>(MockMode.autoUnit)
        val useCase = UpdateMovieLikeUCImpl(dataSource)

        useCase(movie)
        val result = useCase(movie.copy(isLiked = true))

        assertEquals(UpdateMovieResult.Success, result)
        verifySuspend(VerifyMode.exactly(2)) { dataSource.update(any()) }
        verifySuspend { dataSource.update(movie.copy(isLiked = false)) }
    }

    @Test
    fun `invoke should handle movies with same ID but different other properties`() = runTest {
        val dataSource = mock<MovieFavoriteDS>(MockMode.autoUnit)
        val useCase = UpdateMovieLikeUCImpl(dataSource)
        val movie2 = movie.copy(title = "Different Title", rating = 3.0f)

        useCase(movie)
        val result = useCase(movie2)

        assertEquals(UpdateMovieResult.Success, result)
        verifySuspend {
            dataSource.update(movie.copy(isLiked = true))
            dataSource.update(movie2.copy(isLiked = true))
        }
    }
}
