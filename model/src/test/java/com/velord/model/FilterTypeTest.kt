package com.velord.model

import com.velord.model.movie.FilterType
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterTypeTest {

    @Test
    fun `Rating Default has correct values`() {
        val default = FilterType.Rating.Default
        assertEquals(7f, default.start)
        assertEquals(7.1f, default.end)
    }

    @Test
    fun `VoteCount Default has correct values`() {
        val default = FilterType.VoteCount.Default
        assertEquals(100, default.start) // Updated assertion
        assertEquals(200, default.end)   // Updated assertion
    }

    @Test
    fun `createAll returns list with both default filters`() {
        val allFilters = FilterType.createAll()
        assertEquals(2, allFilters.size)
        assertEquals(FilterType.Rating.Default, allFilters[0])
        assertEquals(FilterType.VoteCount.Default, allFilters[1])
    }

    @Test
    fun `Rating constructor enforces valid range and order`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(start = 8f, end = 7f, min = 0f, max = 10f, steps = 100) // Invalid order
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(start = -1f, end = 2f, min = 0f, max = 10f, steps = 100) // Start out of range
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(start = 5f, end = 12f, min = 0f, max = 10f, steps = 100) // End out of range
        }
    }

    @Test
    fun `VoteCount constructor enforces valid range and order`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(start = 300, end = 200, min = 0, max = 1000, steps = 20) // Invalid order
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(start = -10, end = 50, min = 0, max = 1000, steps = 20) // Start out of range
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(start = 500, end = 1200, min = 0, max = 1000, steps = 20) // End out of range
        }
    }

    @Test
    fun `Rating constructor allows valid values`() {
        val rating = FilterType.Rating(start = 3f, end = 8f, min = 0f, max = 10f, steps = 50)
        assertEquals(3f, rating.start)
        assertEquals(8f, rating.end)
    }

    @Test
    fun `VoteCount constructor allows valid values`() {
        val voteCount = FilterType.VoteCount(start = 50, end = 500, min = 0, max = 1000, steps = 10)
        assertEquals(50, voteCount.start)
        assertEquals(500, voteCount.end)
    }

    @Test
    fun `Rating constructor enforces min less than max`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(start = 5f, end = 8f, min = 10f, max = 0f, steps = 50) // Min greater than max
        }
    }

    @Test
    fun `VoteCount constructor enforces min less than max`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(start = 100, end = 200, min = 1000, max = 0, steps = 20) // Min greater than max
        }
    }

    @Test
    fun `Rating steps are positive`() {
        val rating = FilterType.Rating(start = 3f, end = 8f, min = 0f, max = 10f, steps = 50)
        assertTrue(rating.steps > 0)
    }

    @Test
    fun `VoteCount steps are positive`() {
        val voteCount = FilterType.VoteCount(start = 50, end = 500, min = 0, max = 1000, steps = 10)
        assertTrue(voteCount.steps > 0)
    }

    @Test
    fun `Rating start is within min and max`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(start = -1f, end = 5f, min = 0f, max = 10f, steps = 50) // Start below min
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(start = 11f, end = 12f, min = 0f, max = 10f, steps = 50) // Start above max
        }
    }

    @Test
    fun `VoteCount start is within min and max`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(start = -10, end = 100, min = 0, max = 1000, steps = 20) // Start below min
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(start = 1100, end = 1200, min = 0, max = 1000, steps = 20) // Start above max
        }
    }

    @Test
    fun `Rating end is within min and max`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(start = 3f, end = -1f, min = 0f, max = 10f, steps = 50) // End below min
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(start = 8f, end = 12f, min = 0f, max = 10f, steps = 50) // End above max
        }
    }

    @Test
    fun `VoteCount end is within min and max`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(start = 50, end = -10, min = 0, max = 1000, steps = 20) // End below min
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(start = 800, end = 1200, min = 0, max = 1000, steps = 20) // End above max
        }
    }

    @Test
    fun `Rating equality check works correctly`() {
        val rating1 = FilterType.Rating(start = 3f, end = 8f, min = 0f, max = 10f, steps = 50)
        val rating2 = FilterType.Rating(start = 3f, end = 8f, min = 0f, max = 10f, steps = 50)
        val rating3 = FilterType.Rating(start = 4f, end = 9f, min = 0f, max = 10f, steps = 50)

        assertEquals(rating1, rating2) // Same values
        assertNotEquals(rating1, rating3) // Different start and end}
    }

    @Test
    fun `VoteCount equality check works correctly`() {
        val voteCount1 =
            FilterType.VoteCount(start = 50, end = 500, min = 0, max = 1000, steps = 10)
        val voteCount2 =
            FilterType.VoteCount(start = 50, end = 500, min = 0, max = 1000, steps = 10)
        val voteCount3 =
            FilterType.VoteCount(start = 100, end = 600, min = 0, max = 1000, steps = 10)

        assertEquals(voteCount1, voteCount2) // Same values
        assertNotEquals(voteCount1, voteCount3) // Different start and end
    }

    @Test
    fun `Rating toString provides meaningful representation`() {
        val rating = FilterType.Rating(start = 3f, end = 8f, min = 0f, max = 10f, steps = 50)
        val expectedString = "Rating(start=3.0, end=8.0, min=0.0, max=10.0, steps=50)"
        assertEquals(expectedString, rating.toString())
    }

    @Test
    fun `VoteCount toString provides meaningful representation`() {
        val voteCount = FilterType.VoteCount(start = 50, end = 500, min = 0, max = 1000, steps = 10)
        val expectedString = "VoteCount(start=50, end=500, min=0, max=1000, steps=10)"
        assertEquals(expectedString, voteCount.toString())
    }
}