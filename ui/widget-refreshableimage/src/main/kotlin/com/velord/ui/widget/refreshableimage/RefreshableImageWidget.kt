package com.velord.ui.widget.refreshableimage

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.net.toUri
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.velord.core.resource.LocalLocalizationState
import com.velord.core.ui.compose.glance.GlanceWidgetThemeSustainer
import com.velord.ui.widget.refreshableimage.model.ImageParameter
import com.velord.usecase.setting.GetLocalizationStateUC
import org.koin.core.context.GlobalContext

class RefreshableImageWidget :
    GlanceAppWidget(errorUiLayout = R.layout.refreshable_image_widget_error_layout),
    GlanceWidgetThemeSustainer<RefreshableImageWidget> {

    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition
    override val sizeMode: SizeMode = SizeMode.Exact

    override val name: Class<RefreshableImageWidget> = RefreshableImageWidget::class.java
    override val useDarkThemePreferenceKey: Preferences.Key<Boolean> =
        RefreshableImageWidget.useDarkThemePreferenceKey

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val localization = GlobalContext
            .get()
            .get<GetLocalizationStateUC>()()
            .value

        provideContent {
            CompositionLocalProvider(LocalLocalizationState provides localization) {
                RefreshableImageWidgetScreen()
            }
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        RefreshableImageWidgetWorker.cancel(context, glanceId)
    }

    companion object {
        private val sourceUrlPreferenceKey = stringPreferencesKey("image_source_url")
        internal val seedPreferenceKey = stringPreferencesKey("image_seed")
        internal val isDownloadingNewImagePreferenceKey = booleanPreferencesKey(
            "image_is_downloading",
        )
        internal val useDarkThemePreferenceKey = booleanPreferencesKey("use_dark_theme")
        internal val refreshableImageWidgetKey = ActionParameters.Key<ImageParameter>(
            "refreshableImageWidgetKey",
        )

        internal fun getImageUriKey(
            imageParameter: ImageParameter,
        ) = createPreferenceKey(imageParameter)

        private fun createPreferenceKey(imageParameter: ImageParameter) = stringPreferencesKey(
            "uri" +
                "/seed - ${imageParameter.seed}" +
                "/size - w:${imageParameter.getSimpleWidth()}, " +
                "h:${imageParameter.getSimpleHeight()}",
        )

        internal suspend fun updatePreferences(
            context: Context,
            url: String,
            uri: String,
            imageParameter: ImageParameter,
        ) {
            val manager = GlanceAppWidgetManager(context)
            manager.getGlanceIds(RefreshableImageWidget::class.java).forEach {
                updateAppWidgetState(context, it) { prefs ->
                    prefs[sourceUrlPreferenceKey] = url
                    prefs[getImageUriKey(imageParameter)] = uri
                    prefs[seedPreferenceKey] = imageParameter.seed
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
