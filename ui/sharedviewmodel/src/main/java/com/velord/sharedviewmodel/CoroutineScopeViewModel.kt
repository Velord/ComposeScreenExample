package com.velord.sharedviewmodel

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import com.velord.util.exception.BaseException
import com.velord.util.exception.toBaseException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.coroutines.CoroutineContext

private const val TAG = "CoroutineScopeViewModel"
private val log = Logger.withTag(TAG)

open class CoroutineScopeViewModel: ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, error ->
        runCatching {
            val exception = error.toBaseException()
            log.d { "Exception: $exception: $error" }
            exceptionEvent.tryEmit(exception)
        }.onFailure {
            it.printStackTrace()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + errorHandler

    val exceptionEvent = MutableSharedFlow<BaseException>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}
