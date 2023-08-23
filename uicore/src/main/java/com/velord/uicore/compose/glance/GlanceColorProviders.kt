package com.velord.uicore.compose.glance

import androidx.compose.ui.graphics.Color
import androidx.glance.color.colorProviders
import androidx.glance.unit.ColorProvider
import com.velord.uicore.compose.theme.color.DarkBackground
import com.velord.uicore.compose.theme.color.DarkError
import com.velord.uicore.compose.theme.color.DarkErrorContainer
import com.velord.uicore.compose.theme.color.DarkInverseOnSurface
import com.velord.uicore.compose.theme.color.DarkInversePrimary
import com.velord.uicore.compose.theme.color.DarkInverseSurface
import com.velord.uicore.compose.theme.color.DarkOnBackground
import com.velord.uicore.compose.theme.color.DarkOnError
import com.velord.uicore.compose.theme.color.DarkOnErrorContainer
import com.velord.uicore.compose.theme.color.DarkOnPrimary
import com.velord.uicore.compose.theme.color.DarkOnPrimaryContainer
import com.velord.uicore.compose.theme.color.DarkOnSecondary
import com.velord.uicore.compose.theme.color.DarkOnSecondaryContainer
import com.velord.uicore.compose.theme.color.DarkOnSurface
import com.velord.uicore.compose.theme.color.DarkOnSurfaceVariant
import com.velord.uicore.compose.theme.color.DarkOnTertiary
import com.velord.uicore.compose.theme.color.DarkOnTertiaryContainer
import com.velord.uicore.compose.theme.color.DarkOutline
import com.velord.uicore.compose.theme.color.DarkPrimary
import com.velord.uicore.compose.theme.color.DarkPrimaryContainer
import com.velord.uicore.compose.theme.color.DarkSecondary
import com.velord.uicore.compose.theme.color.DarkSecondaryContainer
import com.velord.uicore.compose.theme.color.DarkSurface
import com.velord.uicore.compose.theme.color.DarkSurfaceVariant
import com.velord.uicore.compose.theme.color.DarkTertiary
import com.velord.uicore.compose.theme.color.DarkTertiaryContainer
import com.velord.uicore.compose.theme.color.LightBackground
import com.velord.uicore.compose.theme.color.LightError
import com.velord.uicore.compose.theme.color.LightErrorContainer
import com.velord.uicore.compose.theme.color.LightInverseOnSurface
import com.velord.uicore.compose.theme.color.LightInversePrimary
import com.velord.uicore.compose.theme.color.LightInverseSurface
import com.velord.uicore.compose.theme.color.LightOnBackground
import com.velord.uicore.compose.theme.color.LightOnError
import com.velord.uicore.compose.theme.color.LightOnErrorContainer
import com.velord.uicore.compose.theme.color.LightOnPrimary
import com.velord.uicore.compose.theme.color.LightOnPrimaryContainer
import com.velord.uicore.compose.theme.color.LightOnSecondary
import com.velord.uicore.compose.theme.color.LightOnSecondaryContainer
import com.velord.uicore.compose.theme.color.LightOnSurface
import com.velord.uicore.compose.theme.color.LightOnSurfaceVariant
import com.velord.uicore.compose.theme.color.LightOnTertiary
import com.velord.uicore.compose.theme.color.LightOnTertiaryContainer
import com.velord.uicore.compose.theme.color.LightOutline
import com.velord.uicore.compose.theme.color.LightPrimary
import com.velord.uicore.compose.theme.color.LightPrimaryContainer
import com.velord.uicore.compose.theme.color.LightSecondary
import com.velord.uicore.compose.theme.color.LightSecondaryContainer
import com.velord.uicore.compose.theme.color.LightSurface
import com.velord.uicore.compose.theme.color.LightSurfaceVariant
import com.velord.uicore.compose.theme.color.LightTertiary
import com.velord.uicore.compose.theme.color.LightTertiaryContainer

object GlanceColorProviders {

    fun dark() = colorProviders(
        primary = ColorProvider(Color.DarkPrimary),
        onPrimary = ColorProvider(Color.DarkOnPrimary),
        primaryContainer = ColorProvider(Color.DarkPrimaryContainer),
        onPrimaryContainer = ColorProvider(Color.DarkOnPrimaryContainer),
        inversePrimary = ColorProvider(Color.DarkInversePrimary),
        secondary = ColorProvider(Color.DarkSecondary),
        onSecondary = ColorProvider(Color.DarkOnSecondary),
        secondaryContainer = ColorProvider(Color.DarkSecondaryContainer),
        onSecondaryContainer = ColorProvider(Color.DarkOnSecondaryContainer),
        tertiary = ColorProvider(Color.DarkTertiary),
        onTertiary = ColorProvider(Color.DarkOnTertiary),
        tertiaryContainer = ColorProvider(Color.DarkTertiaryContainer),
        onTertiaryContainer = ColorProvider(Color.DarkOnTertiaryContainer),
        background = ColorProvider(Color.DarkBackground),
        onBackground = ColorProvider(Color.DarkOnBackground),
        surface = ColorProvider(Color.DarkSurface),
        onSurface = ColorProvider(Color.DarkOnSurface),
        surfaceVariant = ColorProvider(Color.DarkSurfaceVariant),
        onSurfaceVariant = ColorProvider(Color.DarkOnSurfaceVariant),
        inverseSurface = ColorProvider(Color.DarkInverseSurface),
        inverseOnSurface = ColorProvider(Color.DarkInverseOnSurface),
        error = ColorProvider(Color.DarkError),
        onError = ColorProvider(Color.DarkOnError),
        errorContainer = ColorProvider(Color.DarkErrorContainer),
        onErrorContainer = ColorProvider(Color.DarkOnErrorContainer),
        outline = ColorProvider(Color.DarkOutline),
    )

    fun light() = colorProviders(
        primary = ColorProvider(Color.LightPrimary),
        onPrimary = ColorProvider(Color.LightOnPrimary),
        primaryContainer = ColorProvider(Color.LightPrimaryContainer),
        onPrimaryContainer = ColorProvider(Color.LightOnPrimaryContainer),
        inversePrimary = ColorProvider(Color.LightInversePrimary),
        secondary = ColorProvider(Color.LightSecondary),
        onSecondary = ColorProvider(Color.LightOnSecondary),
        secondaryContainer = ColorProvider(Color.LightSecondaryContainer),
        onSecondaryContainer = ColorProvider(Color.LightOnSecondaryContainer),
        tertiary = ColorProvider(Color.LightTertiary),
        onTertiary = ColorProvider(Color.LightOnTertiary),
        tertiaryContainer = ColorProvider(Color.LightTertiaryContainer),
        onTertiaryContainer = ColorProvider(Color.LightOnTertiaryContainer),
        background = ColorProvider(Color.LightBackground),
        onBackground = ColorProvider(Color.LightOnBackground),
        surface = ColorProvider(Color.LightSurface),
        onSurface = ColorProvider(Color.LightOnSurface),
        surfaceVariant = ColorProvider(Color.LightSurfaceVariant),
        onSurfaceVariant = ColorProvider(Color.LightOnSurfaceVariant),
        inverseSurface = ColorProvider(Color.LightInverseSurface),
        inverseOnSurface = ColorProvider(Color.LightInverseOnSurface),
        error = ColorProvider(Color.LightError),
        onError = ColorProvider(Color.LightOnError),
        errorContainer = ColorProvider(Color.LightErrorContainer),
        onErrorContainer = ColorProvider(Color.LightOnErrorContainer),
        outline = ColorProvider(Color.LightOutline),
    )
}