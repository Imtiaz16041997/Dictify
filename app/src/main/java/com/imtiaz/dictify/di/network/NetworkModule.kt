package com.imtiaz.dictify.di.network

import com.imtiaz.dictify.data.dataSource.remote.ApiService
import com.imtiaz.dictify.data.dataSource.remote.ApiURL
import com.imtiaz.dictify.data.dataSource.remote.DeeplApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // --- Common Network Components (Unqualified) ---

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    // --- Dictionary API Specific Bindings ---

    @DictionaryApi // Qualifies this specific String instance
    @Singleton
    @Provides
    fun provideDictionaryBaseURL(): String {
        return ApiURL.BASE_URL
    }

    @DictionaryApi // Qualifies this specific OkHttpClient instance
    @Singleton
    @Provides
    fun provideDictionaryOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor // Injects the common, unqualified interceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.callTimeout(40, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(40, TimeUnit.SECONDS)
        okHttpClient.readTimeout(40, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(40, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(loggingInterceptor)
        return okHttpClient.build()
    }

    @DictionaryApi // Qualifies this specific Retrofit instance
    @Singleton
    @Provides
    fun provideDictionaryRetrofitClient(
        @DictionaryApi baseUrl: String, // Injects the qualified dictionary base URL
        @DictionaryApi okHttpClient: OkHttpClient, // Injects the qualified dictionary OkHttpClient
        converterFactory: Converter.Factory // Injects the common, unqualified converter factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    // Provides the DictionaryApiService interface (UNQUALIFIED)
    @Singleton
    @Provides
    fun provideDictionaryApiService(@DictionaryApi retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    // --- Translator API Specific Bindings ---

    @TranslatorApi // Qualifies this specific String instance
    @Singleton
    @Provides
    fun provideTranslatorBaseURL(): String {
        return ApiURL.TRANSLATION_BASE_URL
    }

    @TranslatorApi // Qualifies this specific OkHttpClient instance
    @Singleton
    @Provides
    fun provideTranslatorOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor // Injects the common, unqualified interceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.callTimeout(40, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(40, TimeUnit.SECONDS)
        okHttpClient.readTimeout(40, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(40, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(loggingInterceptor)
        return okHttpClient.build()
    }

    @TranslatorApi // Qualifies this specific Retrofit instance
    @Singleton
    @Provides
    fun provideTranslatorRetrofitClient(
        @TranslatorApi baseUrl: String, // Injects the qualified translator base URL
        @TranslatorApi okHttpClient: OkHttpClient, // Injects the qualified translator OkHttpClient
        converterFactory: Converter.Factory // Injects the common, unqualified converter factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    // Provides the DeeplApiService interface (UNQUALIFIED)
    @Singleton
    @Provides
    fun provideDeeplApiService(@TranslatorApi retrofit: Retrofit): DeeplApiService {
        return retrofit.create(DeeplApiService::class.java)
    }
}