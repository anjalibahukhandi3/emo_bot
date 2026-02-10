package com.example.emotionmonitor.repository

import com.example.emotionmonitor.model.EmotionRequest
import com.example.emotionmonitor.model.EmotionResponse
import com.example.emotionmonitor.network.EmotionApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class EmotionRepository(private val apiService: EmotionApiService) {

    suspend fun analyzeEmotion(text: String): Flow<Result<EmotionResponse>> = flow {
        try {
            val response = apiService.analyzeText(EmotionRequest(text))
            emit(Result.success(response))
        } catch (e: IOException) {
            emit(Result.failure(Exception("Network Error")))
        } catch (e: HttpException) {
            emit(Result.failure(Exception("API Error: ${e.code()}")))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
