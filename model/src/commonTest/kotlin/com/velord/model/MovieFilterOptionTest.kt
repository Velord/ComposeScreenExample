package com.velord.model

import com.velord.model.movie.FilterType
import com.velord.model.movie.MovieFilterOption
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class MovieFilterOptionTest {

    @Test
    fun `createAll returns list with both default filter options`() {
        val optionRoster = MovieFilterOption.ALL
        assertEquals(2, optionRoster.size)
        assertEquals(FilterType.Rating.DEFAULT, optionRoster[0].type)
        assertEquals(FilterType.VoteCount.DEFAULT, optionRoster[1].type)
    }

    @Test
    fun `equality check works correctly`() {
        val option1 = MovieFilterOption(FilterType.Rating.DEFAULT)
        val option2 = MovieFilterOption(FilterType.Rating.DEFAULT)
        val option3 = MovieFilterOption(FilterType.VoteCount.DEFAULT)

        assertEquals(option1, option2) // Same type
        assertNotEquals(option1, option3) // Different type
    }

    @Test
    fun `toString provides meaningful representation`() {
        val option = MovieFilterOption(FilterType.Rating.DEFAULT)
        val expectedString = "MovieFilterOption(type=Rating(start=7.0, end=7.1, " +
            "min=0.0, max=10.0, stepCount=100))"
        assertEquals(expectedString, option.toString())
    }

    @Test
    fun `copy creates a new instance with modified type`() {
        val originalOption = MovieFilterOption(FilterType.Rating.DEFAULT)
        val newType = FilterType.VoteCount.DEFAULT
        val copiedOption = originalOption.copy(type = newType)

        assertNotEquals(originalOption, copiedOption) // Different instances
        assertEquals(newType, copiedOption.type) // Modified type
    }
}
