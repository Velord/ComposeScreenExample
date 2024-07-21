package com.velord.model.movie

sealed class FilterType {

    data class Rating(val start: Float, val end: Float) : FilterType() {
        companion object {
            val Default = Rating(7f, 7.1f)
        }
    }

    data class VoteCount(val start: Int, val end: Int) : FilterType() {
        companion object {
            val Default = VoteCount(5000, 60000)
        }
    }

    companion object {
        fun createAll(): List<FilterType> = listOf(
            Rating.Default,
            VoteCount.Default,
        )
    }
}

data class MovieFilterOption(
    val type: FilterType,
    val isSelected: Boolean
)