package com.velord.widgetcounter

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.Action
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle
import com.velord.core.resource.Res
import com.velord.core.resource.count
import com.velord.core.resource.counter_widget
import com.velord.core.resource.minus_sign
import com.velord.core.resource.plus_sign
import com.velord.core.ui.compose.glance.MainGlanceTheme
import org.jetbrains.compose.resources.stringResource

private fun createAction(count: Int): Action = actionRunCallback<UpdateCountCallback>(
    parameters = actionParametersOf(CounterWidget.actionParameterKey to count)
)

@Composable
internal fun CounterWidgetScreen() {
    val prefs = currentState<Preferences>()
    val count = prefs[CounterWidget.preferenceKey] ?: 0

    MainGlanceTheme(CounterWidget()) {
        Content(count)
    }
}

@Composable
private fun Content(count: Int) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(16.dp)
            .appWidgetBackground()
            .background(GlanceTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.counter_widget),
            modifier = GlanceModifier.padding(top = 8.dp),
            style = TextStyle(
                color = GlanceTheme.colors.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
        )

        Counter(count)
    }
}

@Composable
private fun Counter(count: Int) {
    Text(
        text = stringResource(Res.string.count, count),
        modifier = GlanceModifier.padding(top = 8.dp),
        style = TextStyle(
            textDecoration = TextDecoration.Underline,
            color = GlanceTheme.colors.onSurfaceVariant,
            fontSize = 16.sp,
        ),
    )

    Row(
        modifier = GlanceModifier.fillMaxWidth().padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            text = stringResource(Res.string.plus_sign),
            modifier = GlanceModifier
                .width(100.dp)
                .background(GlanceTheme.colors.surfaceVariant)
                .padding(8.dp),
            onClick = createAction(count + 1)
        )
        Spacer(modifier = GlanceModifier.width(32.dp))
        Button(
            text = stringResource(Res.string.minus_sign),
            modifier = GlanceModifier
                .width(100.dp)
                .background(GlanceTheme.colors.surfaceVariant)
                .padding(8.dp),
            onClick = createAction(count - 1)
        )
    }
}

@Preview
@Composable
private fun ContentPreview() {
    Content(count = 5)
}
