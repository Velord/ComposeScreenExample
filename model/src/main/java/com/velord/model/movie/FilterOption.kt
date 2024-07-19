package com.velord.model.movie

import androidx.annotation.StringRes

data class FilterOption(
    @StringRes val title: Int,
    val isSelected: Boolean
)