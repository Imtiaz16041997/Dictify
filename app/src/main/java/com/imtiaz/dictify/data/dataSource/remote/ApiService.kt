package com.imtiaz.dictify.data.dataSource.remote

import com.imtiaz.dictify.data.model.dictionary.WordResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {


    @GET("entries/en/{word}")
    suspend fun findingWordTranslation(
        @Path("word") textWord: String
    ): List<WordResponse>

}