package com.velord.feature.movie.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.velord.core.resource.Res
import com.velord.core.resource.all
import com.velord.core.resource.favorite
import org.jetbrains.compose.resources.StringResource

enum class MoviePage(
    val titleRes: StringResource,
    val imageRes: ImageVector
) {
    All(
        titleRes = Res.string.all,
        imageRes = Icons.Rounded.List
    ),
    Favorite(
        titleRes = Res.string.favorite,
        imageRes = Icons.Rounded.Favorite
    )
}
