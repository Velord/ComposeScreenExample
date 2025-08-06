package com.velord.feature.movie.model

import androidx.annotation.StringRes
import androidx.compose.runtime.State
import com.velord.core.resource.R
import com.velord.model.movie.FilterType
import com.velord.model.movie.MovieFilterOption

data class MovieFilterOptionUI(
    val type: FilterType,
    @StringRes val name: Int
) {

    fun getMinMaxStr(
        sliderMinState: State<Float>,
        sliderMaxState: State<Float>
    ): Pair<String, String> = when(type) {
        is FilterType.Rating ->
            sliderMinState.value.toString() to sliderMaxState.value.toString()
        is FilterType.VoteCount ->
            sliderMinState.value.toInt().toString() to sliderMaxState.value.toInt().toString()
    }

    fun getRangeStr(
        sliderRangeState: State<ClosedFloatingPointRange<Float>>,
    ): String = when(type) {
        is FilterType.Rating ->
            sliderRangeState.value.toString()
        is FilterType.VoteCount ->
            "${sliderRangeState.value.start.toInt()}..${sliderRangeState.value.endInclusive.toInt()}"
    }

    companion object {
        fun fromDomain(option: MovieFilterOption): MovieFilterOptionUI = MovieFilterOptionUI(
            type = option.type,
            name = when (option.type) {
                is FilterType.Rating -> R.string.filter_by_rating
                is FilterType.VoteCount -> R.string.filter_by_vote_count
            }
        )
    }
}