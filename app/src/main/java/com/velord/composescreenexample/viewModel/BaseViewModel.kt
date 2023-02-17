package com.velord.composescreenexample.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.velord.backend.ext.toBaseException
import com.velord.model.exception.BaseException
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.coroutines.CoroutineContext

open class BaseViewModel: ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, error ->
        runCatching {
            val exception = error.toBaseException()
            Log.d("BaseViewModel", "Exception: $exception: $error")
            exceptionEvent.tryEmit(exception)
        }.onFailure {
            it.printStackTrace()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main + errorHandler

    val exceptionEvent = MutableSharedFlow<BaseException>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}