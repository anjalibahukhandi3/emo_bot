package com.example.emotionmonitor.network

import com.example.emotionmonitor.model.EmotionRequest
import com.example.emotionmonitor.model.EmotionResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface EmotionApiService {
    @POST("analyze")
    suspend fun analyzeText(@Body request: EmotionRequest): EmotionResponse
}
