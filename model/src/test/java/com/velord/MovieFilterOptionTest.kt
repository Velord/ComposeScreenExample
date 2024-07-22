package com.velord

import com.velord.model.movie.FilterType
import com.velord.model.movie.MovieFilterOption
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MovieFilterOptionTest {

    @Test
    fun `can create MovieFilterOption with Rating type and unselected`() {
        val option = MovieFilterOption(FilterType.Rating(6f, 8f), false)
        assertEquals(FilterType.Rating(6f, 8f), option.type)
        assertEquals(false, option.isSelected)
    }

    @Test
    fun `can create MovieFilterOption with VoteCount type and selected`() {
        val option = MovieFilterOption(FilterType.VoteCount(1000, 50000), true)
        assertEquals(FilterType.VoteCount(1000, 50000), option.type)
        assertEquals(true, option.isSelected)
    }

    @Test
    fun `two MovieFilterOptions with same properties should be equal`() {
        val option1 = MovieFilterOption(FilterType.Rating(7f, 8f), true)
        val option2 = MovieFilterOption(FilterType.Rating(7f, 8f), true)
        assertEquals(option1, option2)
    }

    @Test
    fun `two MovieFilterOptions with different properties should not be equal`() {
        val option1 = MovieFilterOption(FilterType.Rating(7f, 8f), true)
        val option2 = MovieFilterOption(FilterType.VoteCount(1000, 50000), true)
        assert(option1 != option2)
    }

    @Test
    fun `MovieFilterOption with different FilterType instances are not equal`() {
        val option1 = MovieFilterOption(FilterType.Rating(7f, 8f), true)
        val option2 = MovieFilterOption(FilterType.Rating(6f, 9f), true) // Different Rating ranges
        assert(option1 != option2)
    }
}