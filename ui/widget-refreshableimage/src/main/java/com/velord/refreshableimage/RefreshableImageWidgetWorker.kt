package com.velord.refreshableimage

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import coil3.imageLoader
import coil3.memory.MemoryCache
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import java.util.concurrent.TimeUnit

private const val SEED_KEY = "seed"
private const val WIDTH_KEY = "width"
private const val HEIGHT_KEY = "height"
private const val FORCE_KEY = "force"

private const val PICSUM_BASE_URL = "https://picsum.photos"

class RefreshableImageWidgetWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        private val uniqueWorkName =
            RefreshableImageWidgetWorker::class.simpleName ?: "RefreshableImageWidgetWorker"

        internal fun createUrl(imageParameters: ImageParameters): String =
            PICSUM_BASE_URL +
                    "/seed/${imageParameters.seed}" +
                    "/${imageParameters.getSimpleWidth()}/${imageParameters.getSimpleHeight()}"

        internal fun enqueue(
            context: Context,
            glanceId: GlanceId,
            parameters: ImageParameters,
            force: Boolean = false
        ) {
            val manager = WorkManager.getInstance(context)
            val requestBuilder = OneTimeWorkRequestBuilder<RefreshableImageWidgetWorker>().apply {
                addTag(glanceId.toString())
                setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                setInputData(
                    Data.Builder()
                        .putString(SEED_KEY, parameters.seed)
                        .putFloat(WIDTH_KEY, parameters.width)
                        .putFloat(HEIGHT_KEY, parameters.height)
                        .putBoolean(FORCE_KEY, force)
                        .build()
                )
            }
            val workPolicy = if (force) {
                ExistingWorkPolicy.REPLACE
            } else {
                ExistingWorkPolicy.KEEP
            }

            val workName = uniqueWorkName + parameters.seed + parameters.width + parameters.height
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

        internal fun cancel(context: Context, glanceId: GlanceId) {
            WorkManager.getInstance(context).cancelAllWorkByTag(glanceId.toString())
        }
    }

    override suspend fun doWork(): Result {
        return try {
            val seed: String = inputData.getString(SEED_KEY) ?: ImageParameters.DEFAULT_SEED
            val width: Float = inputData.getFloat(WIDTH_KEY, 0f)
            val height: Float = inputData.getFloat(HEIGHT_KEY, 0f)
            val force: Boolean = inputData.getBoolean(FORCE_KEY, false)

            val parameters = ImageParameters(seed, width, height)
            val url = createUrl(parameters)
            val uri = fetchImage(url, force)
            Log.d("RefreshableImageWidget", "doWork url: $url\nuri: $uri")

            RefreshableImageWidget.updatePreferences(
                context = context,
                url = url,
                uri = uri,
                parameters = parameters
            )
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    /**
     * Use Coil and Picsum Photos to randomly load images into the cache based on the provided
     * size. This method returns the path of the cached image, which you can send to the widget.
     */
    private suspend fun fetchImage(
        url: String,
        force: Boolean
    ) : String {
        Log.d("RefreshableImageWidget", "doWork url: $url")
        executeRequest(url, force)
        val path = context.getUriForFileThanGrantPermissionThanGetUriPath(url)

        return requireNotNull(path) {
            "Failed to load image from $url"
        }
    }

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
}