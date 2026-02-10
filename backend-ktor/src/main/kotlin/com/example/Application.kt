package com.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class EmotionRequest(val text: String)

@Serializable
data class EmotionResponse(val emotion: String, val confidence: Double)

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    
    routing {
        post("/analyze") {
            val request = call.receive<EmotionRequest>()
            val text = request.text.lowercase()
            
            val response = when {
                listOf("happy", "good", "great", "love", "awesome", "excellent").any { it in text } ->
                    EmotionResponse("Happy", Random.nextDouble(0.7, 1.0))
                
                listOf("sad", "bad", "terrible", "cry", "awful", "depressed").any { it in text } ->
                    EmotionResponse("Sad", Random.nextDouble(0.6, 0.95))
                
                listOf("angry", "mad", "hate", "furious", "annoyed").any { it in text } ->
                    EmotionResponse("Angry", Random.nextDouble(0.6, 0.9))
                
                listOf("fear", "scared", "afraid", "terrified", "nervous").any { it in text } ->
                    EmotionResponse("Fear", Random.nextDouble(0.5, 0.85))
                
                else ->
                    EmotionResponse("Neutral", Random.nextDouble(0.4, 0.8))
            }
            
            call.respond(response)
        }
    }
}
