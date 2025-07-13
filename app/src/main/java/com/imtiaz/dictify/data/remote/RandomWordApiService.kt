package com.imtiaz.dictify.data.remote

import retrofit2.http.GET

interface RandomWordApiService {
    @GET("word")
    suspend fun getRandomWord(): List<String>
}