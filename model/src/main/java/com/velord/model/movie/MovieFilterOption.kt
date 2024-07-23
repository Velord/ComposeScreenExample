package com.velord.model.movie

sealed class FilterType(
    open val start: Number,
    open val end: Number,
    open val min: Number,
    open val max: Number,
    open val steps: Int
) {

    data class Rating(
        override val start: Float,
        override val end: Float,
        override val min: Float,
        override val max: Float,
        override val steps: Int
    ) : FilterType(start, end, min, max, steps) {

        init {
            require(min >= 0.0f)
            require(max <= 10.0f)
            require(start < end)
            require(min < max)
            require(start in min..max)
            require(end in min..max)
        }

        companion object {
            val Default = Rating(
                start = 7f,
                end = 7.1f,
                min = 0f,
                max = 10f,
                steps = 100
            )
        }
    }

    data class VoteCount(
        override val start: Int,
        override val end: Int,
        override val min: Int,
        override val max: Int,
        override val steps: Int
    ) : FilterType(start, end, min, max, steps) {

        init {
            require(min >= 0)
            require(max <= 1000)
            require(start < end)
            require(min < max)
            require(start in min..max)
            require(end in min..max)
        }

        companion object {
            val Default = VoteCount(
                start = 100,
                end = 200,
                min = 0,
                max = 1000,
                steps = 20
            )
        }
    }

    companion object {
        fun createAll(): List<FilterType> = listOf(
            Rating.Default,
            VoteCount.Default,
        )
    }
}

data class MovieFilterOption(val type: FilterType) {
    companion object {
        fun createAll() = listOf(
            MovieFilterOption(FilterType.Rating.Default),
            MovieFilterOption(FilterType.VoteCount.Default),
        )
    }
}