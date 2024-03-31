package com.velord.uicore.compose.theme.shape

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.graphics.Shape
import com.velord.uicore.compose.path.ticketPath

fun TicketShape(cornerRadius: Float): Shape = GenericShape { size, _ ->
    addPath(ticketPath(cornerRadius, size))
}