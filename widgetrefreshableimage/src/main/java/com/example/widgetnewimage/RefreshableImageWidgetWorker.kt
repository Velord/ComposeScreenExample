package com.example.widgetnewimage

import android.content.Context
import androidx.glance.GlanceId
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class RefreshableImageWidgetWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        private val uniqueWorkName = RefreshableImageWidgetWorker::class.simpleName

        fun enqueu(context: Context, glanceId: GlanceId, size: ParametersSize, force: Boolean = false) {

        }
    }

    override suspend fun doWork(): Result {
        return Result.success()
    }
}