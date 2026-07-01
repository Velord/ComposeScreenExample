package com.velord.feature.movie.model

import androidx.compose.runtime.State
import com.velord.core.resource.Res
import com.velord.core.resource.filter_by_rating
import com.velord.core.resource.filter_by_vote_count
import com.velord.model.movie.FilterType
import com.velord.model.movie.MovieFilterOption
import org.jetbrains.compose.resources.StringResource

data class MovieFilterOptionUI(
    val type: FilterType,
    val name: StringResource
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
                is FilterType.Rating -> Res.string.filter_by_rating
                is FilterType.VoteCount -> Res.string.filter_by_vote_count
            }
        )
    }
}
