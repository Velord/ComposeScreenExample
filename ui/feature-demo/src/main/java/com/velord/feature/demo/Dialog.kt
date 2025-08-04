package com.velord.feature.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindowProvider
import com.velord.uicore.compose.preview.PreviewCombined

private const val BACKGROUND_DIALOG_ALPHA = 0.5f

@Composable
fun TwoButtonDialog(
    isVisibleState: State<Boolean>,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    confirmText: String = stringResource(com.velord.resource.R.string.demo),
    cancelText: String = stringResource(com.velord.resource.R.string.all),
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit = onDismissRequest,
    onCancelClick: (() -> Unit)? = onDismissRequest,
    enterAnimation: EnterTransition = fadeIn(),
    exitAnimation: ExitTransition = fadeOut(),
    content: @Composable () -> Unit = {
        Content(
            title = title,
            description = description,
            confirmText = confirmText,
            cancelText = cancelText,
            onConfirmClick = onConfirmClick,
            onCancelClick = onCancelClick,
        )
    }
) {
    Dialog(
        isVisibleState = isVisibleState,
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        enterAnimation = enterAnimation,
        exitAnimation = exitAnimation,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialog(
    isVisibleState: State<Boolean>,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    enterAnimation: EnterTransition = fadeIn(),
    exitAnimation: ExitTransition = fadeOut(),
    backgroundColor: Color = Color.Black.copy(alpha = BACKGROUND_DIALOG_ALPHA),
    backgroundEnterAnimation: EnterTransition = fadeIn(animationSpec = tween(durationMillis = 300)),
    backgroundExitAnimation: ExitTransition = fadeOut(),
    content: @Composable () -> Unit
) {
    val showState = remember { mutableStateOf(false) }

    val isVisible = isVisibleState.value
    LaunchedEffect(isVisible) {
        if (isVisible.not()) return@LaunchedEffect

        showState.value = true
    }

    if (showState.value.not()) return

    DialogSetup(
        isVisibleState = isVisibleState,
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        enterAnimation = enterAnimation,
        exitAnimation = exitAnimation,
        backgroundColor = backgroundColor,
        backgroundEnterAnimation = backgroundEnterAnimation,
        backgroundExitAnimation = backgroundExitAnimation,
        onDispose = { showState.value = false },
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogSetup(
    isVisibleState: State<Boolean>,
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    enterAnimation: EnterTransition,
    exitAnimation: ExitTransition,
    backgroundColor: Color,
    backgroundEnterAnimation: EnterTransition,
    backgroundExitAnimation: ExitTransition,
    onDispose: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val animationInProgressState = remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            animationInProgressState.value = true
        }

        Background(
            isVisibleState = isVisibleState,
            animationInProgressState = animationInProgressState,
            color = backgroundColor,
            enter = backgroundEnterAnimation,
            exit = backgroundExitAnimation,
            onDismissRequest = onDismissRequest
        )

        BasicAlertDialog(onDismissRequest = onDismissRequest) {
            val window = (LocalView.current.parent as? DialogWindowProvider)?.window

            SideEffect {
                window?.apply {
                    setDimAmount(0f)
                    setWindowAnimations(-1)
                }
            }

            AnimatedVisibility(
                visible = animationInProgressState.value && isVisibleState.value,
                enter = enterAnimation,
                exit = exitAnimation
            ) {
                Box(
                    modifier = modifier
                        .pointerInput(Unit) { detectTapGestures { } }
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(28.dp))
                        .clip(RoundedCornerShape(28.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    content()
                }

                DisposableEffect(Unit) {
                    onDispose {
                        onDispose()
                    }
                }
            }
        }
    }
}

@Composable
private fun Background(
    isVisibleState: State<Boolean>,
    animationInProgressState: State<Boolean>,
    color: Color,
    enter: EnterTransition,
    exit: ExitTransition,
    onDismissRequest: () -> Unit
) {
    AnimatedVisibility(
        visible = animationInProgressState.value && isVisibleState.value,
        enter = enter,
        exit = exit,
    ) {
        Box(
            modifier = Modifier
                .pointerInput(Unit) { detectTapGestures { onDismissRequest() } }
                .background(color)
                .fillMaxSize()
        )
    }
}

@Composable
private fun Content(
    title: String,
    description: String?,
    confirmText: String,
    cancelText: String,
    onConfirmClick: () -> Unit,
    onCancelClick: (() -> Unit)?,
) {
    Column(
        modifier = Modifier.padding(vertical = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Title(title = title)
        Spacer(modifier = Modifier.height(16.dp))
        Description(description = description)
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
        ActionButton(
            confirmText = confirmText,
            cancelText = cancelText,
            onConfirmClick = onConfirmClick,
            onCancelClick = onCancelClick,
        )
    }
}

@Composable
private fun Title(title: String) {
    Text(
        modifier = Modifier.padding(horizontal = 24.dp),
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

@Composable
private fun Description(description: String?) {
    if (description.isNullOrEmpty().not()) {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = description,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ColumnScope.ActionButton(
    confirmText: String,
    cancelText: String,
    onConfirmClick: () -> Unit,
    onCancelClick: (() -> Unit)?,
) {
    val buttonsModifier = Modifier
        .align(alignment = Alignment.End)
        .padding(top = 24.dp, start = 24.dp, end = 24.dp)

    if (onCancelClick == null) {
        DialogButton(
            text = confirmText,
            onClick = onConfirmClick,
            modifier = buttonsModifier,
        )
    } else {
        OkAndCancel(
            confirmText = confirmText,
            cancelText = cancelText,
            onConfirmClick = onConfirmClick,
            onCancelClick = onCancelClick,
            modifier = buttonsModifier,
        )
    }
}

@Composable
private fun OkAndCancel(
    confirmText: String,
    cancelText: String,
    onConfirmClick: () -> Unit,
    onCancelClick: (() -> Unit),
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DialogButton(
            text = cancelText,
            onClick = onCancelClick,
        )
        DialogButton(
            text = confirmText,
            onClick = onConfirmClick,
        )
    }
}

@Composable
private fun DialogButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(modifier = modifier, onClick = onClick) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
@PreviewCombined
private fun ForaDialogWithTwoButtonsPreview() {
    MaterialTheme {
        TwoButtonDialog(
            isVisibleState = remember { mutableStateOf(true) },
            title = stringResource(com.velord.resource.R.string.app_name),
            description = stringResource(com.velord.resource.R.string.app_name),
            onDismissRequest = {},
        )
    }
}

@Composable
@PreviewCombined
private fun ForaDialogWithPositiveButtonPreview() {
    MaterialTheme {
        TwoButtonDialog(
            isVisibleState = remember { mutableStateOf(true) },
            title = stringResource(com.velord.resource.R.string.app_name),
            description = stringResource(com.velord.resource.R.string.app_name),
            onDismissRequest = { },
            onCancelClick = null
        )
    }
}