package com.velord.uicore.compose.theme

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.velord.uicore.compose.path.arcAtBottomCenterPath
import com.velord.uicore.compose.path.ticketPath

val MainShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

fun TicketShape(cornerRadius: Float): Shape =
    GenericShape { size, _ ->
        addPath(ticketPath(cornerRadius, size))
    }

fun ArcAtBottomCenterShape(
    progress: Int,
    onEdgeTouch: () -> Unit
) : Shape = GenericShape { size, _ ->
    addPath(arcAtBottomCenterPath(size, progress, onEdgeTouch))
}