package com.velord.ui.feature.demo.dialog.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.velord.core.resource.AppString
import com.velord.core.resource.stringResource
import com.velord.core.ui.compose.preview.PreviewCombined

@Composable
fun OneButtonDialog(
    isVisibleState: State<Boolean>,
    title: String,
    action: DialogActions,
    modifier: DialogModifiers = DialogDefaults.modifiers,
    animation: DialogAnimations = DialogDefaults.animations,
    color: DialogColors = DialogDefaults.colors(),
    style: DialogTextStyles = DialogDefaults.textStyles(),
    message: String? = null,
    positiveText: String = stringResource(AppString.ok),
    content: @Composable () -> Unit = {
        Content(
            title = title,
            message = message,
            positiveText = positiveText,
            negativeText = null,
            action = action,
            modifier = modifier,
            color = color,
            style = style
        )
    }
) {
    Dialog(
        isVisibleState = isVisibleState,
        action = action,
        modifier = modifier,
        animation = animation,
        color = color,
        content = content
    )
}

@Composable
fun TwoButtonDialog(
    isVisibleState: State<Boolean>,
    title: String,
    action: DialogActions,
    modifier: DialogModifiers = DialogDefaults.modifiers,
    animation: DialogAnimations = DialogDefaults.animations,
    color: DialogColors = DialogDefaults.colors(),
    style: DialogTextStyles = DialogDefaults.textStyles(),
    message: String? = null,
    positiveText: String = stringResource(AppString.confirm),
    negativeText: String = stringResource(AppString.cancel),
    content: @Composable () -> Unit = {
        Content(
            title = title,
            message = message,
            positiveText = positiveText,
            negativeText = negativeText,
            action = action,
            modifier = modifier,
            color = color,
            style = style
        )
    }
) {
    Dialog(
        isVisibleState = isVisibleState,
        action = action,
        modifier = modifier,
        animation = animation,
        color = color,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialog(
    isVisibleState: State<Boolean>,
    action: DialogActions,
    modifier: DialogModifiers = DialogDefaults.modifiers,
    animation: DialogAnimations = DialogDefaults.animations,
    color: DialogColors = DialogDefaults.colors(),
    content: @Composable () -> Unit
) {
    val isShowingState = remember { mutableStateOf(false) }

    val isVisible = isVisibleState.value
    LaunchedEffect(isVisible) {
        if (isVisible.not()) return@LaunchedEffect

        isShowingState.value = true
    }

    if (isShowingState.value.not()) return

    DialogSetup(
        isVisibleState = isVisibleState,
        action = action,
        modifier = modifier,
        animation = animation,
        color = color,
        onDispose = { isShowingState.value = false },
    ) {
        content()
    }
}

@PreviewCombined
@Composable
private fun WithTwoButtonsPreview() {
    MaterialTheme {
        TwoButtonDialog(
            isVisibleState = remember { mutableStateOf(true) },
            title = stringResource(AppString.app_name),
            message = stringResource(AppString.app_name),
            action = DialogActions.TwoButton {}
        )
    }
}

@PreviewCombined
@Composable
private fun OneButtonPreview() {
    MaterialTheme {
        OneButtonDialog(
            isVisibleState = remember { mutableStateOf(true) },
            title = stringResource(AppString.app_name),
            message = stringResource(AppString.app_name),
            action = DialogActions.OneButton {}
        )
    }
}
