package com.velord.composescreenexample.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.velord.datastore.AppSettingsDataStoreSerializer
import com.velord.datastore.DataStoreService
import com.velord.datastore.DataStoreServiceImpl
import com.velord.util.settings.AppSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val SETTINGS = "settings"

private val Context.dataStore by dataStore(
    fileName = SETTINGS,
    serializer = AppSettingsDataStoreSerializer
)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<AppSettings> =
        context.dataStore

    @Singleton
    @Provides
    fun provideDataStoreService(prefs: DataStore<AppSettings>): DataStoreService =
        DataStoreServiceImpl(prefs)
}