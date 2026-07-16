package com.velord.ui.widget.refreshableimage

import android.content.Context
import androidx.glance.GlanceId
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import coil3.imageLoader
import coil3.memory.MemoryCache
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import com.velord.ui.widget.refreshableimage.model.ImageParameter
import com.velord.ui.widget.refreshableimage.util.getUriForFileThanGrantPermissionThanGetUriPath
import java.util.concurrent.TimeUnit

private const val SEED_KEY = "seed"
private const val WIDTH_KEY = "width"
private const val HEIGHT_KEY = "height"
private const val FORCE_KEY = "force"

private const val PICSUM_BASE_URL = "https://picsum.photos"
private const val WORKAROUND_DELAY_DAYS = 365L
private const val TAG = "RefreshableImageWidgetWorker"

class RefreshableImageWidgetWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        private val log = Logger.withTag(TAG)

        private val uniqueWorkName = RefreshableImageWidgetWorker::class.simpleName ?: TAG

        internal fun createUrl(imageParameter: ImageParameter): String = PICSUM_BASE_URL +
                "/seed/${imageParameter.seed}" +
                "/${imageParameter.getSimpleWidth()}/${imageParameter.getSimpleHeight()}"

        internal fun enqueue(
            context: Context,
            glanceId: GlanceId,
            imageParameter: ImageParameter,
            force: Boolean = false
        ) {
            val manager = WorkManager.getInstance(context)
            val requestBuilder = OneTimeWorkRequestBuilder<RefreshableImageWidgetWorker>().apply {
                addTag(glanceId.toString())
                setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                setInputData(
                    Data.Builder()
                        .putString(SEED_KEY, imageParameter.seed)
                        .putFloat(WIDTH_KEY, imageParameter.width)
                        .putFloat(HEIGHT_KEY, imageParameter.height)
                        .putBoolean(FORCE_KEY, force)
                        .build()
                )
            }
            val workPolicy = if (force) {
                ExistingWorkPolicy.REPLACE
            } else {
                ExistingWorkPolicy.KEEP
            }

            val workName = uniqueWorkName +
                imageParameter.seed +
                imageParameter.width +
                imageParameter.height
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
                    setInitialDelay(WORKAROUND_DELAY_DAYS, TimeUnit.DAYS)
                }.build(),
            )
        }

        internal fun cancel(context: Context, glanceId: GlanceId) {
            WorkManager.getInstance(context).cancelAllWorkByTag(glanceId.toString())
        }
    }

    override suspend fun doWork(): Result = try {
        val seed: String = inputData.getString(SEED_KEY) ?: ImageParameter.DEFAULT_SEED
        val width: Float = inputData.getFloat(WIDTH_KEY, 0f)
        val height: Float = inputData.getFloat(HEIGHT_KEY, 0f)
        val force: Boolean = inputData.getBoolean(FORCE_KEY, false)

        val imageParameter = ImageParameter(seed, width, height)
        val url = createUrl(imageParameter)
        val uri = fetchImage(url, force)
        log.d { "doWork url: $url\nuri: $uri" }

        RefreshableImageWidget.updatePreferences(
            context = context,
            url = url,
            uri = uri,
            imageParameter = imageParameter,
        )
        Result.success()
    } catch (_: Exception) {
        Result.failure()
    }

    /**
     * Use Coil and Picsum Photos to randomly load images into the cache based on the provided
     * size. This method returns the path of the cached image, which you can send to the widget.
     */
    private suspend fun fetchImage(
        url: String,
        force: Boolean
    ) : String {
        log.d { "doWork url: $url" }
        executeRequest(url, force)
        val path = context.getUriForFileThanGrantPermissionThanGetUriPath(url)

        return requireNotNull(path) {
            "Failed to load image from $url"
        }
    }

    private suspend fun executeRequest(url: String, force: Boolean) {
        val request = ImageRequest.Builder(context).data(url).build()

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
