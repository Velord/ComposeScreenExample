package com.velord.uicore.compose.glance

import androidx.glance.color.colorProviders
import androidx.glance.unit.ColorProvider
import com.velord.uicore.compose.theme.color.ColorDarkTokens
import com.velord.uicore.compose.theme.color.ColorLightTokens

internal object GlanceColorProviders {

    fun dark() = colorProviders(
        primary = ColorProvider(ColorDarkTokens.Primary),
        onPrimary = ColorProvider(ColorDarkTokens.OnPrimary),
        primaryContainer = ColorProvider(ColorDarkTokens.PrimaryContainer),
        onPrimaryContainer = ColorProvider(ColorDarkTokens.OnPrimaryContainer),
        secondary = ColorProvider(ColorDarkTokens.Secondary),
        onSecondary = ColorProvider(ColorDarkTokens.OnSecondary),
        secondaryContainer = ColorProvider(ColorDarkTokens.SecondaryContainer),
        onSecondaryContainer = ColorProvider(ColorDarkTokens.OnSecondaryContainer),
        tertiary = ColorProvider(ColorDarkTokens.Tertiary),
        onTertiary = ColorProvider(ColorDarkTokens.OnTertiary),
        tertiaryContainer = ColorProvider(ColorDarkTokens.TertiaryContainer),
        onTertiaryContainer = ColorProvider(ColorDarkTokens.OnTertiaryContainer),
        error = ColorProvider(ColorDarkTokens.Error),
        errorContainer = ColorProvider(ColorDarkTokens.ErrorContainer),
        onError = ColorProvider(ColorDarkTokens.OnError),
        onErrorContainer = ColorProvider(ColorDarkTokens.OnErrorContainer),
        background = ColorProvider(ColorDarkTokens.Background),
        onBackground = ColorProvider(ColorDarkTokens.OnBackground),
        surface = ColorProvider(ColorDarkTokens.Surface),
        onSurface = ColorProvider(ColorDarkTokens.OnSurface),
        surfaceVariant = ColorProvider(ColorDarkTokens.SurfaceVariant),
        onSurfaceVariant = ColorProvider(ColorDarkTokens.OnSurfaceVariant),
        outline = ColorProvider(ColorDarkTokens.Outline),
        inverseOnSurface = ColorProvider(ColorDarkTokens.InverseOnSurface),
        inverseSurface = ColorProvider(ColorDarkTokens.InverseSurface),
        inversePrimary = ColorProvider(ColorDarkTokens.InversePrimary),
    )
    
    fun light() = colorProviders(
        primary = ColorProvider(ColorLightTokens.Primary),
        onPrimary = ColorProvider(ColorLightTokens.OnPrimary),
        primaryContainer = ColorProvider(ColorLightTokens.PrimaryContainer),
        onPrimaryContainer = ColorProvider(ColorLightTokens.OnPrimaryContainer),
        secondary = ColorProvider(ColorLightTokens.Secondary),
        onSecondary = ColorProvider(ColorLightTokens.OnSecondary),
        secondaryContainer = ColorProvider(ColorLightTokens.SecondaryContainer),
        onSecondaryContainer = ColorProvider(ColorLightTokens.OnSecondaryContainer),
        tertiary = ColorProvider(ColorLightTokens.Tertiary),
        onTertiary = ColorProvider(ColorLightTokens.OnTertiary),
        tertiaryContainer = ColorProvider(ColorLightTokens.TertiaryContainer),
        onTertiaryContainer = ColorProvider(ColorLightTokens.OnTertiaryContainer),
        error = ColorProvider(ColorLightTokens.Error),
        onError = ColorProvider(ColorLightTokens.OnError),
        errorContainer = ColorProvider(ColorLightTokens.ErrorContainer),
        onErrorContainer = ColorProvider(ColorLightTokens.OnErrorContainer),
        background = ColorProvider(ColorLightTokens.Background),
        onBackground = ColorProvider(ColorLightTokens.OnBackground),
        surface = ColorProvider(ColorLightTokens.Surface),
        onSurface = ColorProvider(ColorLightTokens.OnSurface),
        surfaceVariant = ColorProvider(ColorLightTokens.SurfaceVariant),
        onSurfaceVariant = ColorProvider(ColorLightTokens.OnSurfaceVariant),
        outline = ColorProvider(ColorLightTokens.Outline),
        inverseOnSurface = ColorProvider(ColorLightTokens.InverseOnSurface),
        inverseSurface = ColorProvider(ColorLightTokens.InverseSurface),
        inversePrimary = ColorProvider(ColorLightTokens.InversePrimary),
    )
}