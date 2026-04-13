package com.velord.core.ui.compose.shape

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.graphics.Shape
import com.velord.core.ui.annotation.ConstructorLikeFunction
import com.velord.core.ui.compose.path.arcAtBottomCenterPath

@ConstructorLikeFunction
fun ArcAtBottomCenterShape(
    progress: Int,
    onEdgeTouch: () -> Unit
) : Shape = GenericShape { size, _ ->
    addPath(arcAtBottomCenterPath(size, progress, onEdgeTouch))
}
