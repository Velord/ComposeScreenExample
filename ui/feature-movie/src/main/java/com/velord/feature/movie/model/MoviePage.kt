package com.velord.feature.movie.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.velord.core.resource.R

enum class MoviePage(
    @StringRes val titleRes: Int,
    val imageRes: ImageVector
) {
    All(
        titleRes = R.string.all,
        imageRes = Icons.Rounded.List
    ),
    Favorite(
        titleRes = R.string.favorite,
        imageRes = Icons.Rounded.Favorite
    )
}