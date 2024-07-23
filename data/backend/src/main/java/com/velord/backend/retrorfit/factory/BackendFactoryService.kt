package com.velord.backend.retrorfit.factory

import android.content.Context
import com.velord.backend.model.BaseUrl
import com.velord.backend.retrorfit.factory.interceptor.HeaderInterceptor
import com.velord.backend.retrorfit.service.auth.AuthService
import com.velord.backend.retrorfit.service.auth.AuthServiceImpl
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
                it.add(createLoggingInterceptor())
            }
        }
        OkHttpClient(*interceptors.toTypedArray())
    }
    private val retrofit = Retrofit(baseUrl, okHttpClient, createConverterFactory())

    override fun authService(): AuthService = AuthServiceImpl(retrofit.create())
}