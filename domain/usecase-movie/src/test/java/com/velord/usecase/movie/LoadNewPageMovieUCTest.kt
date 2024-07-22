package com.velord.usecase.movie

import com.velord.model.movie.MoviePagination
import com.velord.usecase.movie.dataSource.MovieDS
import com.velord.usecase.movie.result.MovieLoadNewPageResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoadNewPageMovieUCTest {

    @Test
    fun `invoke should return Success when dataSource loads a full page`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { loadNewPage() } returns MoviePagination.PAGE_COUNT
        }

        val loadNewPageMovieUC = LoadNewPageMovieUC(dataSource)
        val result = loadNewPageMovieUC()

        assertEquals(MovieLoadNewPageResult.Success, result)
    }

    @Test
    fun `invoke should return Exausted when dataSource loads less than a full page`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { loadNewPage() } returns MoviePagination.PAGE_COUNT - 10
        }

        val loadNewPageMovieUC = LoadNewPageMovieUC(dataSource)
        val result = loadNewPageMovieUC()

        assertEquals(MovieLoadNewPageResult.Exausted, result)
    }

    @Test
    fun `invoke should return LoadPageFailed when dataSource throws an exception`() = runTest {
        val errorMessage = "Test Error"
        val dataSource = mockk<MovieDS> {
            coEvery { loadNewPage() } throws Exception(errorMessage)
        }

        val loadNewPageMovieUC = LoadNewPageMovieUC(dataSource)
        val result = loadNewPageMovieUC()

        assertEquals(MovieLoadNewPageResult.LoadPageFailed(errorMessage), result)
    }

    @Test
    fun `invoke should return LoadPageFailed with empty message when dataSource throws an exception with empty message`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { loadNewPage() } throws Exception("")
        }

        val loadNewPageMovieUC = LoadNewPageMovieUC(dataSource)
        val result = loadNewPageMovieUC()

        assertEquals(MovieLoadNewPageResult.LoadPageFailed(""), result)
    }

    @Test
    fun `invoke should return LoadPageFailed when dataSource throws an exception with a very long message`() = runTest {
        val veryLongErrorMessage = "This is a very long error message that exceeds any reasonable length and should be truncated or handled appropriately bythe UI to avoid display issues."
        val dataSource = mockk<MovieDS> {
            coEvery { loadNewPage() } throws Exception(veryLongErrorMessage)
        }

        val loadNewPageMovieUC = LoadNewPageMovieUC(dataSource)
        val result = loadNewPageMovieUC()

        assertEquals(MovieLoadNewPageResult.LoadPageFailed(veryLongErrorMessage), result)
        // Assert that the full error message is still captured in the result
    }

    @Test
    fun `invoke should return Exausted when dataSource loads zero new items`() = runTest{
        val dataSource = mockk<MovieDS> {
            coEvery { loadNewPage() } returns 0 // Simulate loading zero items
        }

        val loadNewPageMovieUC = LoadNewPageMovieUC(dataSource)
        val result = loadNewPageMovieUC()

        assertEquals(MovieLoadNewPageResult.Exausted, result) // Expect Exausted as no new items were loaded
    }

    @Test
    fun `invoke returns Success when loaded count is exactly PAGE_COUNT`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { loadNewPage() } returns MoviePagination.PAGE_COUNT
        }
        val loadNewPageMovieUC = LoadNewPageMovieUC(dataSource)
        assertEquals(MovieLoadNewPageResult.Success, loadNewPageMovieUC())
    }

    @Test
    fun`invoke returns Exausted when loaded count is one less than PAGE_COUNT`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { loadNewPage() } returns MoviePagination.PAGE_COUNT - 1
        }
        val loadNewPageMovieUC = LoadNewPageMovieUC(dataSource)
        assertEquals(MovieLoadNewPageResult.Exausted, loadNewPageMovieUC())
    }

    @Test
    fun `invoke returns Success when loaded count is one more than PAGE_COUNT`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { loadNewPage() } returns MoviePagination.PAGE_COUNT + 1
        }
        val loadNewPageMovieUC = LoadNewPageMovieUC(dataSource)
        assertEquals(MovieLoadNewPageResult.Success, loadNewPageMovieUC())
    }

    @Test
    fun `invoke returns Exausted when dataSource returns a negative count`() = runTest {
        val dataSource = mockk<MovieDS> {
            coEvery { loadNewPage() } returns -5 // Simulate an unexpected negative count
        }
        val loadNewPageMovieUC = LoadNewPageMovieUC(dataSource)
        val result = loadNewPageMovieUC()

        assertEquals(MovieLoadNewPageResult.Exausted, result) // Expect Exausted as a negative count indicates no new items
    }
}