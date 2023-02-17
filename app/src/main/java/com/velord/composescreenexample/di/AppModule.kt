package com.velord.composescreenexample.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.velord.datastore.DataStoreService
import com.velord.datastore.DataStoreServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val SETTINGS = "settings"

private val Context.dataStore by preferencesDataStore(name = SETTINGS)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Singleton
    @Provides
    fun provideDataStoreService(prefs: DataStore<Preferences>): DataStoreService =
        DataStoreServiceImpl(prefs)
}