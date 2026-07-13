package com.velord.data.os.share

import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

interface ShareDataSource {
    suspend fun share(text: String)
}

@Module
expect class SharePlatformModule() {
    @Single
    fun provideShareDataSource(scope: Scope): ShareDataSource
}
