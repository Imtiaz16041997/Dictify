package com.imtiaz.dictify.data.dataSource.remote

import com.imtiaz.dictify.data.model.translation.Language
import com.imtiaz.dictify.data.model.translation.LanguageApiDto
import com.imtiaz.dictify.data.model.translation.TranslationRequest
import com.imtiaz.dictify.data.model.translation.TranslationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface DeeplApiService {
    @GET("languages")
    @Headers("Authorization: DeepL-Auth-Key 9309f2a5-ddac-47c0-9307-b7160f516e3a:fx")
    suspend fun getLanguages(): List<LanguageApiDto>

    @POST("translate") // Assuming your translate endpoint is "/translate" and uses POST
    @Headers(
        "Authorization: DeepL-Auth-Key 9309f2a5-ddac-47c0-9307-b7160f516e3a:fx",
        "Content-Type: application/json"
    )
    suspend fun translate(@Body request: TranslationRequest): TranslationResponse
}