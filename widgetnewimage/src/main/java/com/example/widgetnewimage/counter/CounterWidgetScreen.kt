package com.example.widgetnewimage.counter

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalGlanceId
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.LazyListScope
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle

@Composable
internal fun CounterWidgetScreen() {
    val prefs = currentState<Preferences>()
    val count = prefs[countPreferenceKey] ?: 0

    Log.d("NewImageWidget", "Screen: id - ${LocalGlanceId.current}; C - $count")

    GlanceTheme {
        Content(count)
    }
}

@Composable
private fun Content(count: Int) {
    LazyColumn(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(16.dp)
            .appWidgetBackground()
            .background(GlanceTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(
                text = "Counter Widget",
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
        }

        Counter(count)
    }
}

private fun LazyListScope.Counter(count: Int) {
    item {
        Text(
            text = "Count: $count",
            modifier = GlanceModifier.padding(8.dp),
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                color = GlanceTheme.colors.onSurfaceVariant,
                fontSize = 16.sp,
            ),
        )
    }

    item {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                text = "+",
                modifier = GlanceModifier
                    .width(100.dp)
                    .background(GlanceTheme.colors.surfaceVariant)
                    .padding(8.dp),
                onClick = actionRunCallback<UpdateCountCallback>(
                    parameters = actionParametersOf(
                        countWidgetKey to count + 1
                    )
                )
            )
            Button(
                text = "-",
                modifier = GlanceModifier
                    .width(100.dp)
                    .background(GlanceTheme.colors.surfaceVariant)
                    .padding(8.dp),
                onClick = actionRunCallback<UpdateCountCallback>(
                    parameters = actionParametersOf(
                        countWidgetKey to count - 1
                    )
                )
            )
        }
    }
}