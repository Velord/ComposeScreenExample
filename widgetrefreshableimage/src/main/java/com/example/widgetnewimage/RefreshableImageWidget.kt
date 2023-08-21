package com.example.widgetnewimage

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Parcelable
import android.util.Log
import androidx.compose.ui.unit.DpSize
import androidx.core.net.toUri
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.ImageProvider
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.work.CoroutineWorker
import kotlinx.parcelize.Parcelize

@Parcelize
internal class ParametersSize(
    val width: Float,
    val height: Float
) : Parcelable {
    override fun toString(): String = "Width = $width x Height=$height"
}

class RefreshableImageWidget : GlanceAppWidget(errorUiLayout = R.layout.refreshable_image_widget_error_layout) {

    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) =
        provideContent { NewImageWidgetScreen() }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        RefreshableImageWidgetWorker.cancel(context, glanceId)
    }

    companion object {

        internal val sourceUrlKey = stringPreferencesKey("image_source_url")
        internal val refreshableImageWidgetKey = ActionParameters.Key<ParametersSize>("refreshableImageWidgetKey")

        internal fun getImageUriKey(size: DpSize) = createPreferenceKey(size.width.value, size.height.value)

        context(CoroutineWorker)
        internal fun getImageUriKey(width: Float, height: Float) = createPreferenceKey(width, height)

        private fun createPreferenceKey(width: Float, height: Float) =
            stringPreferencesKey("uri - size(w:$width; h:$height)")

        /**
         * Create an ImageProvider using an URI if it's a "content://" type, otherwise load
         * the bitmap from the cache file
         *
         * Note: When using bitmaps directly your might reach the memory limit for RemoteViews.
         * If you do reach the memory limit, you'll need to generate a URI granting permissions
         * to the launcher.
         *
         * More info:
         * https://developer.android.com/training/secure-file-sharing/share-file#GrantPermissions
         */
        fun getImageProvider(path: String): ImageProvider {
            Log.d("RefreshableImageWidget", "getImageProvider path: $path")
            if (path.startsWith("content://"))
                return ImageProvider(path.toUri())

            val bitmap = BitmapFactory.decodeFile(path)
            return ImageProvider(bitmap!!)
        }
    }
}

internal class RefreshCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val newSize: ParametersSize = requireNotNull(parameters[RefreshableImageWidget.refreshableImageWidgetKey]) {
            "Missing refreshableImageWidgetKey"
        }
        Log.d("RefreshableImageWidget", "RefreshCallback.onAction: $glanceId; Size: $newSize")

        RefreshableImageWidgetWorker.enqueue(context, glanceId, newSize, force = true)
    }
}

class RefreshableImageWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = RefreshableImageWidget()
}