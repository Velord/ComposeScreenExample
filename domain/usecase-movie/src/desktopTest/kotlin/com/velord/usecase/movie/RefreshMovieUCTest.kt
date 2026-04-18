package com.velord.usecase.movie

import com.velord.model.movie.MovieRosterSize
import com.velord.usecase.movie.model.MovieLoadNewPageResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RefreshMovieUCTest {

    @Test
    fun `invoke should return success when refresh completes`() = runTest {
        val dataSource = mockk<RefreshMovieDS> {
            coEvery { refresh() } returns MovieRosterSize(1)
        }

        val result = RefreshMovieUCImpl(dataSource).invoke()

        assertEquals(MovieLoadNewPageResult.Success, result)
        coVerify(exactly = 1) { dataSource.refresh() }
    }

    @Test
    fun `invoke should return load page failed when refresh throws`() = runTest {
        val dataSource = mockk<RefreshMovieDS> {
            coEvery { refresh() } throws RuntimeException("refresh error")
        }

        val result = RefreshMovieUCImpl(dataSource).invoke()

        assertEquals(MovieLoadNewPageResult.LoadPageFailed("refresh error"), result)
    }
}
