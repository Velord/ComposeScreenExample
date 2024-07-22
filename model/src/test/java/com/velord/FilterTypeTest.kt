package com.velord

import com.velord.model.movie.FilterType
import junit.framework.TestCase.assertEquals
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
    fun `FilterType subclasses have distinct equality`() {
        val ratingFilter = FilterType.Rating(7f, 8f)
        val voteCountFilter = FilterType.VoteCount(100, 200)

        assert(!ratingFilter.equals(voteCountFilter)) // Explicitly use equals() for comparison
    }
}