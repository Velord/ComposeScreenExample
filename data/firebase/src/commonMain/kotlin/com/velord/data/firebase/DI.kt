@file:Suppress("MatchingDeclarationName")

package com.velord.data.firebase

import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class FirebaseModule {
    @Single
    fun provideFirebaseRemoteConfigDataSource(): FirebaseRemoteConfigDataSource =
        FirebaseRemoteConfigDataSourceImpl()
}
