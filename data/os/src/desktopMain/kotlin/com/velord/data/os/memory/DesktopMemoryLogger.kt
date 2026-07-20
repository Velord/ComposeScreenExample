package com.velord.data.os.memory

import co.touchlab.kermit.Logger
import com.velord.infrastructure.config.BuildConfigResolver

internal class DesktopMemoryLogger(
    private val buildConfigResolver: BuildConfigResolver,
) : MemoryLogger {

    override fun log(message: String, isError: Boolean) {
        if (buildConfigResolver.isLoggingEnabled().not()) return

        if (isError) {
            log.e { message }
        } else {
            log.d { message }
        }
    }

    override fun recordException(throwable: Throwable) {
        if (buildConfigResolver.isLoggingEnabled().not()) return

        log.e(throwable) { "Recording non-fatal exception" }
    }

    override fun setCustomKey(key: String, value: Int) {
        if (buildConfigResolver.isLoggingEnabled().not()) return

        log.d { "Key [$key] = $value" }
    }

    override fun setCustomKey(key: String, value: Long) {
        if (buildConfigResolver.isLoggingEnabled().not()) return

        log.d { "Key [$key] = $value" }
    }

    override fun setCustomKey(key: String, value: String) {
        if (buildConfigResolver.isLoggingEnabled().not()) return

        log.d { "Key [$key] = $value" }
    }

    companion object {
        private const val TAG = "MemoryLogger"
        private val log = Logger.withTag(TAG)
    }
}
