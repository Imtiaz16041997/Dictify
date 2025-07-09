package com.imtiaz.dictify.data.dataSource.remote

import com.imtiaz.dictify.data.model.dictionary.WordResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

//    @GET("languages")
//    suspend fun fetchMultiLanguage(
//    ): LanguageResponse
//
//    @GET("translations?")
//    suspend fun findingWordTranslation(
//        @Query("text") textWord: String,
//        @Query("target") targetDestinationLang: String,
//        @Query("source") sourceDestinationLang: String,
//    ): WordsInformation




    @GET("entries/en/{word}")
    suspend fun findingWordTranslation(
        @Path("word") textWord: String
    ): List<WordResponse>

}