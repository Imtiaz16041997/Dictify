package com.imtiaz.dictify.data.dataSource.remote

import com.imtiaz.dictify.data.model.LanguageResponse
import com.imtiaz.dictify.data.model.WordsInformation
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("languages")
    suspend fun fetchMultiLanguage(
    ): LanguageResponse

    @GET("translations?")
    suspend fun findingWordTranslation(
        @Query("text") textWord: String,
        @Query("target") targetDestinationLang: String,
        @Query("source") sourceDestinationLang: String,
    ): WordsInformation



}