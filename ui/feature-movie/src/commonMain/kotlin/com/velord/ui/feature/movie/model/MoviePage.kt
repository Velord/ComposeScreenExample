package com.velord.ui.feature.movie.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.velord.core.resource.AppString
import com.velord.core.resource.AppStringResource

enum class MoviePage(
    val titleRes: AppStringResource,
    val imageRes: ImageVector
) {
    All(
        titleRes = AppString.all,
        imageRes = Icons.Rounded.List
    ),
    Favorite(
        titleRes = AppString.favorite,
        imageRes = Icons.Rounded.Favorite
    )
}
