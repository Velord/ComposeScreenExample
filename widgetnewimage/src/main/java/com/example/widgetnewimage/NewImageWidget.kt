package com.example.widgetnewimage

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle

private val refreshWidgetKey = ActionParameters.Key<String>("refreshNewImageWidgetKey")
private val refreshPreferenceKey = stringPreferencesKey("refreshNewImagePreferenceKey")

private val countWidgetKey = ActionParameters.Key<Int>("countNewImageWidgetKey")
private val countPreferenceKey = intPreferencesKey("countNewImagePreferenceKey")

class NewImageWidget : GlanceAppWidget(errorUiLayout = R.layout.new_image_widget_error_layout) {

    companion object {
        private val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        private val HORIZONTAL_RECTANGLE = DpSize(250.dp, 100.dp)
        private val BIG_SQUARE = DpSize(250.dp, 250.dp)
    }

    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(SMALL_SQUARE, HORIZONTAL_RECTANGLE, BIG_SQUARE)
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        TODO("Not yet implemented")
    }

    @Composable
    fun Content() {
        val prefs = currentState<Preferences>()
        val count = prefs[countPreferenceKey] ?: 0
        val refresh = prefs[refreshPreferenceKey] ?: ""

        Content(
            count = count,
            refresh = refresh,
        )
    }
}

@Preview
@Composable
internal fun Content(
    count: Int = 0,
    refresh: String = "",
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Responsive layout",
            style = TextStyle(textDecoration = TextDecoration.Underline)
        )
        Text(
            text = "Count: $count",
            modifier = GlanceModifier.padding(8.dp),
            style = TextStyle(textDecoration = TextDecoration.Underline)
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
            text = stringResource(id = R.string.refresh),
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

class RefreshCallback: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.d("NewImageWidget", "RefreshCallback.onAction: $glanceId")

    }
}

class UpdateCountCallback: ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.d("NewImageWidget", "UpdateCountCallback.onAction: $glanceId")

        val newCount = requireNotNull(parameters[countWidgetKey]) {
            "Missing countWidgetKey"
        }

        updateAppWidgetState(
            context = context,
            definition = PreferencesGlanceStateDefinition,
            glanceId = glanceId,
        ) {
            it.toMutablePreferences().apply {
                this[countPreferenceKey] = newCount
            }
        }

        NewImageWidget().update(context, glanceId)
    }
}

class NewImageWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NewImageWidget()
}