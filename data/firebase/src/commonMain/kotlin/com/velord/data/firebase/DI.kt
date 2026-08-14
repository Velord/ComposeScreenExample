@file:Suppress("MatchingDeclarationName")

package com.velord.data.firebase

import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [FirebasePlatformModule::class])
class FirebaseModule

@Module
expect class FirebasePlatformModule() {
    @Single
    fun provideFirebaseRemoteConfigDataSource(): FirebaseRemoteConfigDataSource
}
