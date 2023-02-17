package com.velord.backend.factory.interceptor

import android.content.Context
import com.velord.backend.ext.getPackageInfoCompat
import okhttp3.Headers.Companion.toHeaders
import okhttp3.Interceptor
import okhttp3.Response

private const val VERSION = "version"

class HeaderInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val versionName: String = context
            .packageManager
            .getPackageInfoCompat(context.packageName, 0).versionName
        val headers = mutableMapOf(VERSION to versionName)

        val requestBuilder = originalRequest
            .newBuilder()
            .headers(headers.toHeaders())
        return chain.proceed(requestBuilder.build())
    }
}
