package com.example.widgetnewimage

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.ErrorResult
import coil.request.ImageRequest
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

private const val WIDTH_KEY = "width"
private const val HEIGHT_KEY = "height"
private const val FORCE_KEY = "force"

private const val PICSUM_BASE_URL = "https://picsum.photos/"

class RefreshableImageWidgetWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        private val uniqueWorkName =
            RefreshableImageWidgetWorker::class.simpleName ?: "RefreshableImageWidgetWorker"

        fun enqueue(
            context: Context,
            glanceId: GlanceId,
            size: ParametersSize,
            force: Boolean = false
        ) {
            val manager = WorkManager.getInstance(context)
            val requestBuilder = OneTimeWorkRequestBuilder<RefreshableImageWidgetWorker>().apply {
                addTag(glanceId.toString())
                setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                setInputData(
                    Data.Builder()
                        .putFloat(WIDTH_KEY, size.width)
                        .putFloat(HEIGHT_KEY, size.height)
                        .putBoolean(FORCE_KEY, force)
                        .build()
                )
            }
            val workPolicy = if (force) {
                ExistingWorkPolicy.REPLACE
            } else {
                ExistingWorkPolicy.KEEP
            }

            val workName = uniqueWorkName + size.width + size.height
            manager.enqueueUniqueWork(
                workName,
                workPolicy,
                requestBuilder.build()
            )

            // Temporary workaround to avoid WM provider to disable itself and trigger an
            // app widget update
            manager.enqueueUniqueWork(
                "$uniqueWorkName-workaround",
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<RefreshableImageWidgetWorker>().apply {
                    setInitialDelay(365, TimeUnit.DAYS)
                }.build(),
            )
        }

        fun cancel(context: Context, glanceId: GlanceId) {
            WorkManager.getInstance(context).cancelAllWorkByTag(glanceId.toString())
        }
    }

    override suspend fun doWork(): Result {
        return try {
            val width: Float = inputData.getFloat(WIDTH_KEY, 0f)
            val height: Float = inputData.getFloat(HEIGHT_KEY, 0f)
            val force: Boolean = inputData.getBoolean(FORCE_KEY, false)
            val uri = getRandomImage(width, height, force)
            updateRefreshableImageWidget(width, height, uri)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    /**
     * Use Coil and Picsum Photos to randomly load images into the cache based on the provided
     * size. This method returns the path of the cached image, which you can send to the widget.
     */
    private suspend fun getRandomImage(
        width: Float,
        height: Float,
        force: Boolean
    ) : String {
        val url = createUrl(width, height)
        executeRequest(url, force)
        val path = context.getUriForFileThanGrantPermissionThanGetUriPath(url)

        return requireNotNull(path) {
            "Failed to load image from $url"
        }
    }

    private fun createUrl(width: Float, height: Float): String =
        "$PICSUM_BASE_URL${width.roundToInt()}/${height.roundToInt()}"

    private suspend fun executeRequest(url: String, force: Boolean) {
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()

        // Request the image to be loaded and throw error if it failed
        with(context.imageLoader) {
            if (force) {
                diskCache?.remove(url)
                memoryCache?.remove(MemoryCache.Key(url))
            }

            val result = execute(request)
            if (result is ErrorResult)
                throw result.throwable
        }
    }

    private suspend fun updateRefreshableImageWidget(
        width: Float,
        height: Float,
        uri: String
    ) {
        val manager = GlanceAppWidgetManager(context)
        manager.getGlanceIds(RefreshableImageWidget::class.java).forEach {
            updateAppWidgetState(context, it) { prefs ->
                prefs[RefreshableImageWidget.getImageUriKey(width, height)] = uri
                prefs[RefreshableImageWidget.sourceUrlKey] = createUrl(width, height)
            }
        }
        RefreshableImageWidget().updateAll(context)
    }
}