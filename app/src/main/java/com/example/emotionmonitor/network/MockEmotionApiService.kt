package com.example.emotionmonitor.network

import com.example.emotionmonitor.model.EmotionRequest
import com.example.emotionmonitor.model.EmotionResponse
import kotlinx.coroutines.delay
import kotlin.random.Random

class MockEmotionApiService : EmotionApiService {
    override suspend fun analyzeText(request: EmotionRequest): EmotionResponse {
        delay(1000)

        val text = request.text.lowercase()
        return when {
            text.contains("happy") || text.contains("good") || text.contains("love") -> 
                EmotionResponse("Happy", Random.nextFloat() * 0.5f + 0.5f)
            text.contains("sad") || text.contains("bad") || text.contains("cry") -> 
                EmotionResponse("Sad", Random.nextFloat() * 0.5f + 0.5f)
            text.contains("angry") || text.contains("hate") || text.contains("mad") -> 
                EmotionResponse("Angry", Random.nextFloat() * 0.5f + 0.5f)
            text.contains("scared") || text.contains("fear") || text.contains("afraid") -> 
                EmotionResponse("Fear", Random.nextFloat() * 0.5f + 0.5f)
            else -> 
                EmotionResponse("Neutral", Random.nextFloat() * 0.3f + 0.7f)
        }
    }
}
