package com.imtiaz.dictify.di.network

import com.imtiaz.dictify.data.dataSource.remote.ApiService
import com.imtiaz.dictify.data.dataSource.remote.ApiURL
import com.imtiaz.dictify.data.dataSource.remote.DeeplApiService
import com.imtiaz.dictify.data.remote.RandomWordApiService
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

    @DictionaryApi
    @Singleton
    @Provides
    fun provideDictionaryBaseURL(): String {
        return ApiURL.BASE_URL
    }

    @DictionaryApi
    @Singleton
    @Provides
    fun provideDictionaryOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.callTimeout(40, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(40, TimeUnit.SECONDS)
        okHttpClient.readTimeout(40, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(40, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(loggingInterceptor)
        return okHttpClient.build()
    }

    @DictionaryApi
    @Singleton
    @Provides
    fun provideDictionaryRetrofitClient(
        @DictionaryApi baseUrl: String,
        @DictionaryApi okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideDictionaryApiService(@DictionaryApi retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    // --- Translator API Specific Bindings ---

    @TranslatorApi
    @Singleton
    @Provides
    fun provideTranslatorBaseURL(): String {
        return ApiURL.TRANSLATION_BASE_URL
    }

    @TranslatorApi
    @Singleton
    @Provides
    fun provideTranslatorOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.callTimeout(40, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(40, TimeUnit.SECONDS)
        okHttpClient.readTimeout(40, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(40, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(loggingInterceptor)
        return okHttpClient.build()
    }

    @TranslatorApi
    @Singleton
    @Provides
    fun provideTranslatorRetrofitClient(
        @TranslatorApi baseUrl: String,
        @TranslatorApi okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideDeeplApiService(@TranslatorApi retrofit: Retrofit): DeeplApiService {
        return retrofit.create(DeeplApiService::class.java)
    }

    // --- NEW: Random Word API Specific Bindings ---

    @RandomWordApi // Qualifies this specific String instance
    @Singleton
    @Provides
    fun provideRandomWordBaseURL(): String {
        return ApiURL.RANDOM_WORD_BASE_URL // <--- Define this in ApiURL
    }

    @RandomWordApi // Qualifies this specific OkHttpClient instance
    @Singleton
    @Provides
    fun provideRandomWordOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        // You can reuse the common OkHttpClient builder or customize if needed
        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.callTimeout(40, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(40, TimeUnit.SECONDS)
        okHttpClient.readTimeout(40, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(40, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(loggingInterceptor)
        return okHttpClient.build()
    }

    @RandomWordApi // Qualifies this specific Retrofit instance
    @Singleton
    @Provides
    fun provideRandomWordRetrofitClient(
        @RandomWordApi baseUrl: String,
        @RandomWordApi okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideRandomWordApiService(@RandomWordApi retrofit: Retrofit): RandomWordApiService {
        return retrofit.create(RandomWordApiService::class.java)
    }
}