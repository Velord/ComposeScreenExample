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
            require(min >= MIN_RATING)
            require(max <= MAX_RATING)
            require(start < end)
            require(min < max)
            require(start in min..max)
            require(end in min..max)
        }

        companion object {
            private const val MIN_RATING = 0.0f
            private const val MAX_RATING = 10.0f
            private const val DEFAULT_START = 7f
            private const val DEFAULT_END = 7.1f
            private const val DEFAULT_STEPS = 100

            val Default = Rating(
                start = DEFAULT_START,
                end = DEFAULT_END,
                min = MIN_RATING,
                max = MAX_RATING,
                steps = DEFAULT_STEPS
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
            require(min >= MIN_VOTE_COUNT)
            require(max <= MAX_VOTE_COUNT)
            require(start < end)
            require(min < max)
            require(start in min..max)
            require(end in min..max)
        }

        companion object {
            private const val MIN_VOTE_COUNT = 0
            private const val MAX_VOTE_COUNT = 1000
            private const val DEFAULT_START = 100
            private const val DEFAULT_END = 200
            private const val DEFAULT_STEPS = 20

            val Default = VoteCount(
                start = DEFAULT_START,
                end = DEFAULT_END,
                min = MIN_VOTE_COUNT,
                max = MAX_VOTE_COUNT,
                steps = DEFAULT_STEPS
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
