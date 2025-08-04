package com.velord.dialogDemo.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
internal fun Content(
    title: String,
    message: String?,
    positiveText: String,
    negativeText: String?,
    modifier: DialogModifiers,
    action: DialogActions,
    color: DialogColors,
    style: DialogTextStyles,
) {
    Column(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Title(
            text = title,
            modifier = modifier.title,
            color = color.title,
            style = style.title
        )
        Message(
            text = message,
            modifier = modifier.message,
            color = color.message,
            style = style.message
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(
            modifier = modifier.divider,
            color = color.divider
        )
        ButtonRow(
            positiveText = positiveText,
            negativeText = negativeText,
            action = action,
            modifier = modifier,
            color = color,
            style = style,
        )
    }
}

@Composable
private fun Title(
    text: String,
    modifier: Modifier,
    color: Color,
    style: TextStyle
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
    )
}

@Composable
private fun Message(
    text: String?,
    modifier: Modifier,
    color: Color,
    style: TextStyle
) {
    if (text.isNullOrEmpty()) return

    Spacer(modifier = Modifier.height(16.dp))
    Text(
        modifier = modifier,
        text = text,
        color = color,
        style = style,
    )
}

@Composable
private fun ColumnScope.ButtonRow(
    positiveText: String,
    negativeText: String?,
    action: DialogActions,
    modifier: DialogModifiers,
    color: DialogColors,
    style: DialogTextStyles,
) {
    Row(
        modifier = modifier.buttonContainer,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (action.onNegativeClick == null || negativeText.isNullOrEmpty()) {
            DialogButton(
                text = positiveText,
                modifier = modifier.positiveButton,
                color = color.positiveButton,
                style = style.positiveButton,
                onClick = action.onPositiveClick,
            )
        } else {
            DialogButton(
                text = negativeText,
                modifier = modifier.negativeButton,
                color = color.negativeButton,
                style = style.negativeButton,
                onClick = action.onNegativeClick,
            )
            DialogButton(
                text = positiveText,
                modifier = modifier.positiveButton,
                color = color.positiveButton,
                style = style.positiveButton,
                onClick = action.onPositiveClick,
            )
        }
    }
}

@Composable
private fun DialogButton(
    text: String,
    modifier: Modifier,
    color: Color,
    style: TextStyle,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(
            text = text,
            color = color,
            style = style,
        )
    }
}