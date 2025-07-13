package com.imtiaz.dictify.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
//            .addHeader("x-rapidapi-host", "dictzilla-dictionary-api-v2-by-apirobots.p.rapidapi.com")
//            .addHeader("x-rapidapi-key", BuildConfig.API_KEY)
            .build()

        return chain.proceed(newRequest)
    }
}