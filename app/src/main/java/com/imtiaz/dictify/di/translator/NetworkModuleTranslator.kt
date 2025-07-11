package com.imtiaz.dictify.di.translator


import com.imtiaz.dictify.data.dataSource.remote.ApiURL
import com.imtiaz.dictify.data.dataSource.remote.DeeplApiService
import com.imtiaz.dictify.di.qualifiers.TranslatorApi
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
object NetworkModuleTranslator {

    @TranslatorApi
    @Singleton
    @Provides
    fun provideTranslatorBaseURL(): String {
        return ApiURL.TRANSLATION_BASE_URL
    }

    // REMOVE provideTranslatorLoggingInterceptor if using CommonNetworkModule's provideLoggingInterceptor
    // If you need a *specific* logging interceptor for translator, qualify it:
    // @TranslatorApi
    // @Singleton
    // @Provides
    // fun provideTranslatorLoggingInterceptor(): HttpLoggingInterceptor {
    //     return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    // }

    @TranslatorApi
    @Singleton
    @Provides
    fun provideTranslatorOkHttpClient(
        // Inject the unqualified logging interceptor from CommonNetworkModule
        loggingInterceptor: HttpLoggingInterceptor,
        // If you used a qualified logging interceptor above, you'd do:
        // @TranslatorApi loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.callTimeout(40, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(40, TimeUnit.SECONDS)
        okHttpClient.readTimeout(40, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(40, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(loggingInterceptor)
        return okHttpClient.build()
    }

    // REMOVE provideTranslatorConverterFactory if using CommonNetworkModule's provideConverterFactory
    // If you need a *specific* converter factory for translator, qualify it:
    // @TranslatorApi
    // @Singleton
    // @Provides
    // fun provideTranslatorConverterFactory(): Converter.Factory {
    //     return GsonConverterFactory.create()
    // }

    @TranslatorApi // This provides an @TranslatorApi Retrofit
    @Singleton
    @Provides
    fun provideTranslatorRetrofitClient(
        @TranslatorApi baseUrl: String, // Consuming the qualified BaseURL
        @TranslatorApi okHttpClient: OkHttpClient, // Consuming the qualified OkHttpClient
        converterFactory: Converter.Factory, // Consuming the unqualified Converter.Factory from CommonNetworkModule
        // If you used a qualified converter factory above, you'd do:
        // @TranslatorApi converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @TranslatorApi // <--- IMPORTANT: Qualify the RETURN TYPE here!
    @Singleton
    @Provides
    fun provideTranslatorRestApiService(@TranslatorApi retrofit: Retrofit): DeeplApiService {
        return retrofit.create(DeeplApiService::class.java)
    }
}