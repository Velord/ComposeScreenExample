package com.velord.model.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class FilterType {
    abstract val start: Number
    abstract val end: Number
    abstract val min: Number
    abstract val max: Number
    abstract val stepCount: Int

    @Serializable
    @SerialName("Rating")
    data class Rating(
        override val start: Float,
        override val end: Float,
        override val min: Float,
        override val max: Float,
        override val stepCount: Int
    ) : FilterType() {

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
            private const val DEFAULT_STEP_COUNT = 100

            val DEFAULT = Rating(
                start = DEFAULT_START,
                end = DEFAULT_END,
                min = MIN_RATING,
                max = MAX_RATING,
                stepCount = DEFAULT_STEP_COUNT,
            )
        }
    }

    @Serializable
    @SerialName("VoteCount")
    data class VoteCount(
        override val start: Int,
        override val end: Int,
        override val min: Int,
        override val max: Int,
        override val stepCount: Int
    ) : FilterType() {

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
            private const val DEFAULT_STEP_COUNT = 20

            val DEFAULT = VoteCount(
                start = DEFAULT_START,
                end = DEFAULT_END,
                min = MIN_VOTE_COUNT,
                max = MAX_VOTE_COUNT,
                stepCount = DEFAULT_STEP_COUNT,
            )
        }
    }

    companion object {
        val ALL: List<FilterType> get() = filterTypeRoster()

        private fun filterTypeRoster(): List<FilterType> = listOf(
            Rating.DEFAULT,
            VoteCount.DEFAULT,
        )
    }
}

@Serializable
data class MovieFilterOption(val type: FilterType) {

    companion object {
        val ALL = listOf(
            MovieFilterOption(FilterType.Rating.DEFAULT),
            MovieFilterOption(FilterType.VoteCount.DEFAULT),
        )
    }
}
