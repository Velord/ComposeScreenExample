package com.example.widgetnewimage

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Parcelable
import android.util.Log
import androidx.compose.ui.unit.DpSize
import androidx.core.net.toUri
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.velord.uicore.compose.glance.GlanceWidgetThemeSustainer
import kotlinx.parcelize.Parcelize

@Parcelize
internal class ImageParameters(
    val seed: String,
    val width: Float,
    val height: Float
) : Parcelable {

    constructor(seed: String, size: DpSize) :
            this(seed, width = size.width.value, height = size.height.value)

    override fun toString(): String = "Seed = $seed x Width = $width x Height=$height"

    fun getSimpleWidth() = width.toInt()

    fun getSimpleHeight() = height.toInt()

    companion object {
        const val DEFAULT_SEED = "seed"
    }
}

class RefreshableImageWidget : GlanceAppWidget(errorUiLayout = R.layout.refreshable_image_widget_error_layout), GlanceWidgetThemeSustainer<RefreshableImageWidget> {
    // GlanceAppWidget
    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition
    override val sizeMode: SizeMode = SizeMode.Exact
    // GlanceWidgetThemeSustainer
    override val name: Class<RefreshableImageWidget> = RefreshableImageWidget::class.java
    override val useDarkThemePreferenceKey: Preferences.Key<Boolean> = RefreshableImageWidget.useDarkThemePreferenceKey

    override suspend fun provideGlance(context: Context, id: GlanceId) =
        provideContent { NewImageWidgetScreen() }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        RefreshableImageWidgetWorker.cancel(context, glanceId)
    }

    companion object {
        // Preferences keys
        private val sourceUrlPreferenceKey = stringPreferencesKey("image_source_url")
        internal val seedPreferenceKey = stringPreferencesKey("image_seed")
        internal val isDownloadingNewImagePreferenceKey = booleanPreferencesKey("image_is_downloading")
        internal val useDarkThemePreferenceKey = booleanPreferencesKey("use_dark_theme")
        // ActionParameters keys
        internal val refreshableImageWidgetKey = ActionParameters.Key<ImageParameters>("refreshableImageWidgetKey")

        internal fun getImageUriKey(imageParameters: ImageParameters) = createPreferenceKey(imageParameters)

        private fun createPreferenceKey(imageParameters: ImageParameters) =
            stringPreferencesKey("uri" +
                    "/seed - ${imageParameters.seed}" +
                    "/size - w:${imageParameters.getSimpleWidth()}, h:${imageParameters.getSimpleHeight()}")


        internal suspend fun updatePreferences(
            context: Context,
            url: String,
            uri: String,
            parameters: ImageParameters,
        ) {
            val manager = GlanceAppWidgetManager(context)
            manager.getGlanceIds(RefreshableImageWidget::class.java).forEach {
                updateAppWidgetState(context, it) { prefs ->
                    prefs[sourceUrlPreferenceKey] = url
                    prefs[getImageUriKey(parameters)] = uri
                    prefs[seedPreferenceKey] = parameters.seed
                    prefs[isDownloadingNewImagePreferenceKey] = false
                }
            }
            RefreshableImageWidget().updateAll(context)
        }

        /**
         * https://github.com/android/platform-samples/blob/main/samples/user-interface/appwidgets/src/main/java/com/example/platform/ui/appwidgets/glance/image/ImageGlanceWidget.kt
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
        fun getImageProvider(context: Context, path: String): ImageProvider {
            /**
             * https://stackoverflow.com/questions/74361073/how-to-load-images-from-the-internet-into-a-widget-with-jetpack-glance
             * UriImageProvider doesn't work
             * Always return ImageProvider with bitmap inside
             */

            val bitmap = context.contentResolver.openInputStream(path.toUri()).use { data ->
                BitmapFactory.decodeStream(data)
            }
            return ImageProvider(bitmap)
        }
    }
}

internal class RefreshCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val newParameters: ImageParameters = requireNotNull(parameters[RefreshableImageWidget.refreshableImageWidgetKey]) {
            "Missing refreshableImageWidgetKey"
        }
        Log.d("RefreshableImageWidget", "RefreshCallback.onAction: $glanceId; Size: $newParameters")

        val manager = GlanceAppWidgetManager(context)
        manager.getGlanceIds(RefreshableImageWidget::class.java).forEach {
            updateAppWidgetState(context, it) { prefs ->
                prefs[RefreshableImageWidget.isDownloadingNewImagePreferenceKey] = true
            }
        }
        RefreshableImageWidget().update(context, glanceId)
        RefreshableImageWidgetWorker.enqueue(context, glanceId, newParameters, force = true)
    }
}

class RefreshableImageWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = RefreshableImageWidget()
}