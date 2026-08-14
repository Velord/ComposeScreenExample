@file:Suppress("MatchingDeclarationName")

package com.velord.data.firebase

import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
actual class FirebasePlatformModule {
    @Single
    actual fun provideFirebaseRemoteConfigDataSource(): FirebaseRemoteConfigDataSource =
        DesktopFirebaseRemoteConfigDataSource()
}
