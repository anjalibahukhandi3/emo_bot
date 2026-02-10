package com.example.emotionmonitor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emotionmonitor.network.RetrofitClient
import com.example.emotionmonitor.repository.EmotionRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val currentInput: String = "",
    val isLoading: Boolean = false,
    val latestEmotion: String? = null,
    val latestConfidence: Float? = null
)

data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val isUser: Boolean,
    val emotion: String? = null,
    val confidence: Float? = null
)

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
}

class EmotionViewModel : ViewModel() {

    private val repository = EmotionRepository(RetrofitClient.apiService)

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onInputChange(newText: String) {
        _uiState.update { it.copy(currentInput = newText) }
    }

    fun sendMessage() {
        val text = _uiState.value.currentInput.trim()
        if (text.isBlank()) {
            viewModelScope.launch {
                _uiEvent.send(UiEvent.ShowSnackbar("Message cannot be empty"))
            }
            return
        }

        val userMessage = ChatMessage(text = text, isUser = true)
        
        _uiState.update { 
            it.copy(
                messages = it.messages + userMessage,
                currentInput = "",
                isLoading = true
            ) 
        }

        viewModelScope.launch {
            repository.analyzeEmotion(text).collect { result ->
                result.onSuccess { response ->
                    val emotion = response.emotion.replaceFirstChar { it.uppercase() }
                    
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            latestEmotion = emotion,
                            latestConfidence = response.confidence,
                            messages = state.messages.map { msg ->
                                if (msg.id == userMessage.id) {
                                    msg.copy(emotion = emotion, confidence = response.confidence)
                                } else msg
                            }
                        )
                    }
                }.onFailure { exception ->
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.send(UiEvent.ShowSnackbar(exception.message ?: "Unknown error occurred"))
                }
            }
        }
    }
    
    fun clearError() {
    }
}
