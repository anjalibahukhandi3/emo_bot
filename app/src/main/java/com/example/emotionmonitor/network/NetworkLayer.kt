package com.example.emotionmonitor.network

import com.example.emotionmonitor.model.EmotionRequest
import com.example.emotionmonitor.model.EmotionResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface EmotionApiService {
    @POST("analyze")
    suspend fun analyzeText(@Body request: EmotionRequest): EmotionResponse
}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/" // Local Ktor Backend (Emulator)

    val apiService: EmotionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmotionApiService::class.java)
    }
}
