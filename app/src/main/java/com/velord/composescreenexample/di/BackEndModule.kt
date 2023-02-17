package com.velord.composescreenexample.di

import android.content.Context
import com.velord.backend.factory.BackendFactoryService
import com.velord.backend.factory.BackendFactoryServiceImpl
import com.velord.backend.model.BaseUrl
import com.velord.backend.service.auth.AuthService
import com.velord.composescreenexample.BuildConfig
import com.velord.datastore.DataStoreService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackEndModule {

    @Singleton
    @Provides
    fun baseUrl(): BaseUrl = BaseUrl(BuildConfig.BASE_URL)

    @Singleton
    @Provides
    fun provideBackendFactoryService(
        baseUrl: BaseUrl,
        @ApplicationContext context: Context,
        dataStoreService: DataStoreService,
    ): BackendFactoryService = BackendFactoryServiceImpl(
        baseUrl,
        context,
        BuildConfig.IS_LOGGING_ENABLED,
        dataStoreService
    )

    @Singleton
    @Provides
    fun provideAuthService(
        factory: BackendFactoryService
    ): AuthService = factory.authService()
}