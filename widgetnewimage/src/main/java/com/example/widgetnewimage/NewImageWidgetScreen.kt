package com.example.widgetnewimage

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

@Composable
internal fun NewImageWidgetScreen() {
    Log.d("NewImageWidget", "Screen")
    val prefs = currentState<Preferences>()
    val count = prefs[countPreferenceKey] ?: 0
    val refresh = prefs[refreshPreferenceKey] ?: ""

    Content(
        count = count,
        refresh = refresh,
    )
}

@Composable
internal fun Content(
    count: Int,
    refresh: String,
) {
    Log.d("NewImageWidget", "Content: $count, $refresh")

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Responsive NewImageWidget",
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                color = ColorProvider(Color.Cyan),
                fontSize = 18.sp,
            ),
        )
        Text(
            text = "Count: $count",
            modifier = GlanceModifier.padding(8.dp),
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                color = ColorProvider(Color.Red),
                fontSize = 22.sp,
            ),
        )

        Button(
            text = "+",
            modifier = GlanceModifier
                .padding(8.dp)
                .fillMaxWidth(),
            onClick = actionRunCallback<UpdateCountCallback>(
                parameters = actionParametersOf(
                    countWidgetKey to count + 1
                )
            )
        )
        Spacer(modifier = GlanceModifier.padding(8.dp))
        Button(
            text = "-",
            modifier = GlanceModifier
                .padding(8.dp)
                .fillMaxWidth(),
            onClick = actionRunCallback<UpdateCountCallback>(
                parameters = actionParametersOf(
                    countWidgetKey to count - 1
                )
            )
        )

        Button(
            text = LocalContext.current.getString(R.string.refresh),
            onClick = actionRunCallback<RefreshCallback>(
                parameters = actionParametersOf(
                    refreshWidgetKey to "RefreshCallback"
                )
            )
        )

        val size = LocalSize.current
        Text(text = "${size.width} x ${size.height}")
    }
}

@Preview
@Composable
private fun ContentPreview() {
    Content(
        count = 0,
        refresh = "",
    )
}