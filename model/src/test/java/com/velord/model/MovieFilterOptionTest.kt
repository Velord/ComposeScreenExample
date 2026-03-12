package com.velord.model

import com.velord.model.movie.FilterType
import com.velord.model.movie.MovieFilterOption
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class MovieFilterOptionTest {

    @Test
    fun `createAll returns list with both default filter options`() {
        val allOptions = MovieFilterOption.createAll()
        assertEquals(2, allOptions.size)
        assertEquals(FilterType.Rating.Default,allOptions[0].type)
        assertEquals(FilterType.VoteCount.Default, allOptions[1].type)
    }

    @Test
    fun `equality check works correctly`() {
        val option1 = MovieFilterOption(FilterType.Rating.Default)
        val option2 = MovieFilterOption(FilterType.Rating.Default)
        val option3 = MovieFilterOption(FilterType.VoteCount.Default)

        assertEquals(option1, option2) // Same type
        assertNotEquals(option1, option3) // Different type
    }

    @Test
    fun `toString provides meaningful representation`() {
        val option = MovieFilterOption(FilterType.Rating.Default)
        val expectedString = "MovieFilterOption(type=Rating(start=7.0, end=7.1, min=0.0, max=10.0, steps=100))"
        assertEquals(expectedString, option.toString())
    }

    @Test
    fun `copy creates a new instance with modified type`() {
        val originalOption = MovieFilterOption(FilterType.Rating.Default)
        val newType = FilterType.VoteCount.Default
        val copiedOption = originalOption.copy(type = newType)

        assertNotEquals(originalOption, copiedOption) // Different instances
        assertEquals(newType, copiedOption.type) // Modified type
    }
}