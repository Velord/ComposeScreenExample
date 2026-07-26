package com.velord.model

import com.velord.model.movie.FilterType
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterTypeTest {

    @Test
    fun `Rating DEFAULT has correct values`() {
        val default = FilterType.Rating.DEFAULT
        assertEquals(7f, default.start)
        assertEquals(7.1f, default.end)
    }

    @Test
    fun `VoteCount DEFAULT has correct values`() {
        val default = FilterType.VoteCount.DEFAULT
        assertEquals(100, default.start) // Updated assertion
        assertEquals(200, default.end)   // Updated assertion
    }

    @Test
    fun `createAll returns list with both default filters`() {
        val filterRoster = FilterType.ALL
        assertEquals(2, filterRoster.size)
        assertEquals(FilterType.Rating.DEFAULT, filterRoster[0])
        assertEquals(FilterType.VoteCount.DEFAULT, filterRoster[1])
    }

    @Test
    fun `Rating constructor enforces valid range and order`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(8f, 7f, 0f, 10f, 100) // Invalid order
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(-1f, 2f, 0f, 10f, 100) // Start out of range
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(5f, 12f, 0f, 10f, 100) // End out of range
        }
    }

    @Test
    fun `VoteCount constructor enforces valid range and order`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(300, 200, 0, 1000, 20) // Invalid order
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(-10, 50, 0, 1000, 20) // Start out of range
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(500, 1200, 0, 1000, 20) // End out of range
        }
    }

    @Test
    fun `Rating constructor allows valid values`() {
        val rating = FilterType.Rating(start = 3f, end = 8f, min = 0f, max = 10f, stepCount = 50)
        assertEquals(3f, rating.start)
        assertEquals(8f, rating.end)
    }

    @Test
    fun `VoteCount constructor allows valid values`() {
        val voteCount = FilterType.VoteCount(50, 500, 0, 1000, 10)
        assertEquals(50, voteCount.start)
        assertEquals(500, voteCount.end)
    }

    @Test
    fun `Rating constructor enforces min less than max`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(5f, 8f, 10f, 0f, 50) // Min greater than max
        }
    }

    @Test
    fun `VoteCount constructor enforces min less than max`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(100, 200, 1000, 0, 20) // Min greater than max
        }
    }

    @Test
    fun `Rating steps are positive`() {
        val rating = FilterType.Rating(start = 3f, end = 8f, min = 0f, max = 10f, stepCount = 50)
        assertTrue(rating.stepCount > 0)
    }

    @Test
    fun `VoteCount steps are positive`() {
        val voteCount = FilterType.VoteCount(50, 500, 0, 1000, 10)
        assertTrue(voteCount.stepCount > 0)
    }

    @Test
    fun `Rating start is within min and max`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(-1f, 5f, 0f, 10f, 50) // Start below min
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(11f, 12f, 0f, 10f, 50) // Start above max
        }
    }

    @Test
    fun `VoteCount start is within min and max`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(-10, 100, 0, 1000, 20) // Start below min
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(1100, 1200, 0, 1000, 20) // Start above max
        }
    }

    @Test
    fun `Rating end is within min and max`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(3f, -1f, 0f, 10f, 50) // End below min
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.Rating(8f, 12f, 0f, 10f, 50) // End above max
        }
    }

    @Test
    fun `VoteCount end is within min and max`() {
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(50, -10, 0, 1000, 20) // End below min
        }
        assertThrows(IllegalArgumentException::class.java) {
            FilterType.VoteCount(800, 1200, 0, 1000, 20) // End above max
        }
    }

    @Test
    fun `Rating equality check works correctly`() {
        val rating1 = FilterType.Rating(start = 3f, end = 8f, min = 0f, max = 10f, stepCount = 50)
        val rating2 = FilterType.Rating(start = 3f, end = 8f, min = 0f, max = 10f, stepCount = 50)
        val rating3 = FilterType.Rating(start = 4f, end = 9f, min = 0f, max = 10f, stepCount = 50)

        assertEquals(rating1, rating2) // Same values
        assertNotEquals(rating1, rating3) // Different start and end}
    }

    @Test
    fun `VoteCount equality check works correctly`() {
        val voteCount1 = FilterType.VoteCount(50, 500, 0, 1000, 10)
        val voteCount2 = FilterType.VoteCount(50, 500, 0, 1000, 10)
        val voteCount3 = FilterType.VoteCount(100, 600, 0, 1000, 10)

        assertEquals(voteCount1, voteCount2) // Same values
        assertNotEquals(voteCount1, voteCount3) // Different start and end
    }

    @Test
    fun `Rating toString provides meaningful representation`() {
        val rating = FilterType.Rating(start = 3f, end = 8f, min = 0f, max = 10f, stepCount = 50)
        val expectedString = "Rating(start=3.0, end=8.0, min=0.0, max=10.0, stepCount=50)"
        assertEquals(expectedString, rating.toString())
    }

    @Test
    fun `VoteCount toString provides meaningful representation`() {
        val voteCount = FilterType.VoteCount(50, 500, 0, 1000, 10)
        val expectedString = "VoteCount(start=50, end=500, min=0, max=1000, stepCount=10)"
        assertEquals(expectedString, voteCount.toString())
    }
}
