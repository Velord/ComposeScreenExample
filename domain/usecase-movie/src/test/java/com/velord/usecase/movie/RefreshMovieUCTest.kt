package com.velord.usecase.movie

import com.velord.usecase.movie.dataSource.MovieDS
import com.velord.usecase.movie.result.MovieLoadNewPageResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RefreshMovieUCTest {

    @Test
    fun `invoke should return Success when refresh completes successfully`() = runTest {
        val dataSource= mockk<MovieDS> {
            coEvery { refresh() } returns 10 // Assuming a successful refresh returns a positive number
        }
        val refreshMovieUC = RefreshMovieUC(dataSource)
        val result = refreshMovieUC()
        assertEquals(MovieLoadNewPageResult.Success, result)
        coVerify(exactly = 1) { dataSource.refresh() }
    }

    @Test
    fun `invoke should return LoadPageFailed when refresh throws an exception`() = runTest {
        val errorMessage = "Refresh Error"
        val dataSource = mockk<MovieDS> {
            coEvery { refresh() } throws Exception(errorMessage)
        }
        val refreshMovieUC = RefreshMovieUC(dataSource)
        val result = refreshMovieUC()
        assertEquals(MovieLoadNewPageResult.LoadPageFailed(errorMessage), result)
    }

    @Test
    fun `invoke should return LoadPageFailed with empty message when refresh throws an exception with empty message`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { refresh() } throws Exception("")
        }
        val refreshMovieUC = RefreshMovieUC(dataSource)
        val result = refreshMovieUC()
        assertEquals(MovieLoadNewPageResult.LoadPageFailed(""), result)
    }

    @Test
    fun `invoke should return Success when refresh returns zero`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { refresh() } returns 0
        }
        val refreshMovieUC = RefreshMovieUC(dataSource)
        val result = refreshMovieUC()
        assertEquals(MovieLoadNewPageResult.Success, result)
    }

    @Test
    fun `invoke should return Success when refresh returns a large positive number`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { refresh() } returns 1000
        }
        val refreshMovieUC = RefreshMovieUC(dataSource)
        val result = refreshMovieUC()
        assertEquals(MovieLoadNewPageResult.Success, result)
    }

    @Test
    fun `invokeshould return Success when refresh returns the minimum integer value`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { refresh() } returns Int.MIN_VALUE
        }
        val refreshMovieUC = RefreshMovieUC(dataSource)
        val result = refreshMovieUC()
        assertEquals(MovieLoadNewPageResult.Success, result)
    }

    @Test
    fun `invoke should return Success when refresh returns the maximum integer value`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { refresh() } returns Int.MAX_VALUE
        }
        val refreshMovieUC = RefreshMovieUC(dataSource)
        val result = refreshMovieUC()
        assertEquals(MovieLoadNewPageResult.Success, result)
    }

    @Test
    fun `invoke should not modify the result if refresh is called multiple times`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { refresh() } returnsMany listOf(5, 10, 15)
        }
        val refreshMovieUC = RefreshMovieUC(dataSource)
        repeat(3) {val result = refreshMovieUC()
            assertEquals(MovieLoadNewPageResult.Success, result)
        }
    }

    @Test
    fun `invoke should handle consecutive calls to refresh`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { refresh() } returnsMany listOf(5, 10)
        }
        val refreshMovieUC = RefreshMovieUC(dataSource)
        assertEquals(MovieLoadNewPageResult.Success, refreshMovieUC())
        assertEquals(MovieLoadNewPageResult.Success, refreshMovieUC())
    }

    @Test
    fun `invoke should handle refresh being called after a delay`() = runTest {
        val dataSource = mockk<MovieDS>{
            coEvery { refresh() } coAnswers {
                delay(1000) // Introduce a 1-second delay within refresh()
                5
            }
        }
        val refreshMovieUC = RefreshMovieUC(dataSource)
        val result = refreshMovieUC()
        assertEquals(MovieLoadNewPageResult.Success, result)
    }

}