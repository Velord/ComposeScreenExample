package com.velord.os

interface MemoryLogger {
    fun log(message: String, isError: Boolean = false)
    fun recordException(throwable: Throwable)
    fun setCustomKey(key: String, value: Int)

    fun setCustomKey(key: String, value: Long)
    fun setCustomKey(key: String, value: String)
}
