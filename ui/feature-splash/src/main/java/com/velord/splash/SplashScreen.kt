package com.velord.splash

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.resource.R
import com.velord.uicore.compose.preview.PreviewCombined
import com.velord.uicore.utils.LocalTheme
import com.velord.util.settings.SpecialTheme

private const val ANIMATION_TRANSITION_TO_CURRENT_THEME = 1000
private const val ROTATE_AFTER = ANIMATION_TRANSITION_TO_CURRENT_THEME + 1000
/*
Splash screen is the most refined copy representation of the OS splash screen.
When app renders first frame, it will show this screen.
Initialize any data you need than change isAppReadyState to true.

 Android <12 has no "blinking" effect, but Android 12+ has it.
    1. Imitate original window background color
    2. Imitate original icon color, size, crop
    3. Imitate original brand icon color, size
 */
@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    contentWhenAppIsReady: @Composable () -> Unit
) {
    val isAppReadyState = viewModel.isAppReadyFlow.collectAsStateWithLifecycle()

    if (isAppReadyState.value) {
        contentWhenAppIsReady()
    } else {
        Content()
    }
}

@Composable
private fun Content() {
    // Original background color is defined in the theme from Splash Api
    val splashWindowBackground = colorResource(id = R.color.splash_background)
    // If you need to change color to your own, you can use this state
    // Thus smooth color transition will be provided
    val colorState = remember { mutableStateOf(splashWindowBackground) }
    val backgroundColorState = animateColorAsState(
        targetValue = colorState.value,
        animationSpec = tween(
            durationMillis = ANIMATION_TRANSITION_TO_CURRENT_THEME,
            easing = LinearEasing
        ),
        label = "Background color state"
    )
    // Current(Os or user) theme background color
    val themeBackground = MaterialTheme.colorScheme.surface
    SideEffect {
        colorState.value = themeBackground
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColorState.value
    ) {
        Box(Modifier.fillMaxSize()) {
            SplashImitate()
        }
    }
}

@Composable
private fun BoxScope.SplashImitate() {
    // Original icon color used by Splash Api
    val originalIconColor = colorResource(id = R.color.splash_icon_brand)
    val colorState = remember { mutableStateOf(originalIconColor) }
    val tintColorState = animateColorAsState(
        targetValue = colorState.value,
        animationSpec = tween(
            durationMillis = ANIMATION_TRANSITION_TO_CURRENT_THEME,
            easing = LinearEasing
        ),
        label = "CenterIcon color state"
    )

    val isAppHasNativeSplashScreen = remember {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    // Current(Os or user) theme icon color
    val iconTint = MaterialTheme.colorScheme.onSurface
    SideEffect {
        colorState.value = iconTint
    }

    CenterIcon()
    if (isAppHasNativeSplashScreen) {
        BrandIcon(tintColorState)
    }
}

@Composable
private fun BoxScope.CenterIcon() {
    // Define any animation you need specifically for this icon
    val infiniteTransition = rememberInfiniteTransition(label = "icon angle transition")
    val angleState = infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            initialStartOffset = StartOffset(ROTATE_AFTER)
        ),
        label = "rotation angle"
    )

    val modifier = Modifier
        .align(Alignment.Center)
        .padding(bottom = 4.dp)
    IconMultipleColor(
        content = {
            Box(
                modifier = modifier
                    .size(192.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .scale(2.65f)
                        .rotate(angleState.value)
                )
            }
        },
        tailoredForThemeContent = {
            Box(
                modifier = modifier
            ) {
                Image(
                    painter = getIconPainter(),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .scale(2.65f)
                        .rotate(angleState.value)
                )
            }
        }
    )
}

// If icon has ONLY ONE color need to use transition color
@Composable
private fun BoxScope.BrandIcon(colorState: State<Color>) {
    Icon(
        painter = painterResource(id = R.drawable.ic_brand),
        contentDescription = "Brand logo",
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 60.dp)
            .size(width = 200.dp, height = 80.dp)

        ,
        tint = colorState.value
    )
}

// If icon has several colors need to use animated visibility
// as there is no animation between painters in Compose.
@Composable
private fun BoxScope.IconMultipleColor(
    content: @Composable AnimatedVisibilityScope.() -> Unit,
    tailoredForThemeContent: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val visibleState = remember { mutableStateOf(false) }
    SideEffect {
        visibleState.value = true
    }
    // Original icon color used by Splash Api
    IconAnimated(
        isVisible = visibleState.value.not(),
        content = content
    )
    // Our own icon based on current theme
    IconAnimated(
        isVisible = visibleState.value,
        content = tailoredForThemeContent
    )
}

@Composable
private fun BoxScope.IconAnimated(
    isVisible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        modifier = Modifier.align(Alignment.Center),
        enter = fadeIn(animationSpec = tween(durationMillis = 50, delayMillis = ANIMATION_TRANSITION_TO_CURRENT_THEME / 4)),
        exit = fadeOut(animationSpec = tween(durationMillis = ANIMATION_TRANSITION_TO_CURRENT_THEME))
    ) {
        content()
    }
}

@Composable
private fun getIconPainter(): Painter {
    val themeConfig = LocalTheme.current
    val res = if (themeConfig.config.abideToOs)  {
        if (isSystemInDarkTheme()) {
            R.drawable.ic_launcher_foreground_light
        } else {
            R.drawable.ic_launcher_foreground_dark
        }
    } else {
        when (themeConfig.config.current) {
            SpecialTheme.LIGHT -> R.drawable.ic_launcher_foreground_light
            SpecialTheme.DARK -> R.drawable.ic_launcher_foreground_dark
        }
    }
    return painterResource(res)
}

@Composable
@PreviewCombined
private fun Preview() {
    Content()
}