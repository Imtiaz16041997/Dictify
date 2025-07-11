package com.imtiaz.dictify.data.dataSource.remote

import com.imtiaz.dictify.data.model.translation.Language
import retrofit2.http.GET
import retrofit2.http.Headers

interface DeeplApiService {
    @GET("languages")
    @Headers("Authorization: DeepL-Auth-Key 9309f2a5-ddac-47c0-9307-b7160f516e3a:fx")
    suspend fun getLanguages(): List<Language>
}