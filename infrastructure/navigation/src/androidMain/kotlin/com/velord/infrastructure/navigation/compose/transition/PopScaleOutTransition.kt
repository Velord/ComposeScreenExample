package com.velord.infrastructure.navigation.compose.transition

import androidx.compose.animation.scaleOut
import androidx.compose.ui.graphics.TransformOrigin

internal val popScaleOutTransition = scaleOut(
    targetScale = 0.9f,
    transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0.5f),
)
