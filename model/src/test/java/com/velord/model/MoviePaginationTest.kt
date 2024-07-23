package com.velord.model

import com.velord.model.movie.MoviePagination
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MoviePaginationTest {

    @Test
    fun `shouldLoadMore returns true when close to end`() {val result = MoviePagination.shouldLoadMore(
        lastVisibleIndex = 290,
        totalItemCount = 300
    )
        assertEquals(true, result)
    }

    @Test
    fun `shouldLoadMore returns false when far from end`() {
        val result = MoviePagination.shouldLoadMore(
            lastVisibleIndex = 100,
            totalItemCount = 300
        )
        assertEquals(false, result)
    }

    @Test
    fun `shouldLoadMore returns true when at the exact preload threshold`() {val result = MoviePagination.shouldLoadMore(
        lastVisibleIndex = 290,
        totalItemCount = 300
    )
        assertEquals(true, result)
    }

    @Test
    fun `shouldLoadMore returns false when lastVisibleIndex is negative`() {
        val result = MoviePagination.shouldLoadMore(
            lastVisibleIndex = -1, // Invalid index
            totalItemCount = 300
        )
        assertEquals(false, result)
    }

    @Test(expected = IllegalStateException::class) // Expecting an error
    fun `shouldLoadMore throws error when lastVisibleIndex and totalItemCount are both zero`() {
        MoviePagination.shouldLoadMore(
            lastVisibleIndex = 0,
            totalItemCount = 0
        )
    }

    @Test
    fun `shouldLoadMore returns true when lastVisibleIndex is slightly less than totalItemCount`() {
        val result = MoviePagination.shouldLoadMore(
            lastVisibleIndex = 298,
            totalItemCount = 300
        )
        assertEquals(true, result)
    }

    @Test
    fun `shouldLoadMore returns true when lastVisibleIndex is one less than totalItemCount`() {
        val result = MoviePagination.shouldLoadMore(
            lastVisibleIndex = 299,
            totalItemCount = 300
        )
        assertEquals(true, result)
    }
}