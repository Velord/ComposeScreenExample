package com.velord.backend.factory

import android.content.Context
import com.velord.backend.factory.interceptor.HeaderInterceptor
import com.velord.backend.model.BaseUrl
import com.velord.backend.service.auth.AuthService
import com.velord.backend.service.auth.AuthServiceImpl
import com.velord.datastore.DataStoreService
import okhttp3.Interceptor
import retrofit2.create

interface BackendFactoryService {
    fun authService() : AuthService
}

class BackendFactoryServiceImpl(
    baseUrl: BaseUrl,
    private val context: Context,
    private val enableLogging: Boolean,
    private val dataStore: DataStoreService,
) : BackendFactoryService {

    private val okHttpClient = run {
        val interceptors: MutableList<Interceptor> = mutableListOf(
            HeaderInterceptor(context) as Interceptor
        ).also {
            if (enableLogging) {
                it.add(LoggingInterceptor())
            }
        }
        OkHttpClient(*interceptors.toTypedArray())
    }
    private val retrofit = Retrofit(baseUrl, okHttpClient, ConverterFactory())

    override fun authService(): AuthService = AuthServiceImpl(retrofit.create())
}