package com.velord.data.os.memory

import co.touchlab.kermit.Logger
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.velord.infrastructure.config.BuildConfigResolver

internal class AndroidMemoryLogger(
    private val buildConfigResolver: BuildConfigResolver,
) : MemoryLogger {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun log(message: String, isError: Boolean) {
        val isLoggingEnabled = buildConfigResolver.isLoggingEnabled()

        if (isError) {
            if (isLoggingEnabled) log.e { message }
            crashlytics.log("E/$TAG: $message")
        } else {
            if (isLoggingEnabled) log.d { message }
            crashlytics.log("D/$TAG: $message")
        }
    }

    override fun recordException(throwable: Throwable) {
        if (buildConfigResolver.isLoggingEnabled()) {
            log.e(throwable) { "Recording non-fatal exception" }
        }
        crashlytics.recordException(throwable)
    }

    override fun setCustomKey(key: String, value: Int) {
        if (buildConfigResolver.isLoggingEnabled()) log.d { "Key [$key] = $value" }
        crashlytics.setCustomKey(key, value)
    }

    override fun setCustomKey(key: String, value: Long) {
        if (buildConfigResolver.isLoggingEnabled()) log.d { "Key [$key] = $value" }
        crashlytics.setCustomKey(key, value)
    }

    override fun setCustomKey(key: String, value: String) {
        if (buildConfigResolver.isLoggingEnabled()) log.d { "Key [$key] = $value" }
        crashlytics.setCustomKey(key, value)
    }

    companion object {
        private const val TAG = "MemoryLogger"
        private val log = Logger.withTag(TAG)
    }
}
