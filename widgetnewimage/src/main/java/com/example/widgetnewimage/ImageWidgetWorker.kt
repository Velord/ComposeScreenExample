package com.example.widgetnewimage

import android.content.Context
import androidx.compose.ui.unit.DpSize
import androidx.glance.GlanceId
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ImageWidgetWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        private val uniqueWorkName = ImageWidgetWorker::class.simpleName

        fun enqueu(context: Context, size: DpSize, glanceId: GlanceId, force: Boolean = false) {

        }
    }

    override suspend fun doWork(): Result {
        return Result.success()
    }
}