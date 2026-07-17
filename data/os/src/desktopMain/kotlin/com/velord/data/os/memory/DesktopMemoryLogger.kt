package com.velord.data.os.memory

import co.touchlab.kermit.Logger

class DesktopMemoryLogger : MemoryLogger {

    override fun log(message: String, isError: Boolean) {
        if (isError) {
            log.e { message }
        } else {
            log.d { message }
        }
    }

    override fun recordException(throwable: Throwable) {
        log.e(throwable) { "Recording non-fatal exception" }
    }

    override fun setCustomKey(key: String, value: Int) {
        log.d { "Key [$key] = $value" }
    }

    override fun setCustomKey(key: String, value: Long) {
        log.d { "Key [$key] = $value" }
    }

    override fun setCustomKey(key: String, value: String) {
        log.d { "Key [$key] = $value" }
    }

    companion object {
        private const val TAG = "MemoryLogger"
        private val log = Logger.withTag(TAG)
    }
}
