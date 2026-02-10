package com.example.emotionmonitor.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.emotionmonitor.viewmodel.ChatMessage
import com.example.emotionmonitor.viewmodel.EmotionViewModel
import com.example.emotionmonitor.viewmodel.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: EmotionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Emotion Monitor")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            AnimatedVisibility(visible = uiState.latestEmotion != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = getEmotionColor(uiState.latestEmotion)
                            .copy(alpha = 0.15f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Latest Analysis",
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Text(
                                    text = "${uiState.latestEmotion ?: "Neutral"}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = getEmotionColor(uiState.latestEmotion)
                                )
                            }

                            VerticalDivider(modifier = Modifier.height(30.dp))

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Live Emotion Score",
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Text(
                                    text = "${uiState.liveScore}%",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Black,
                                    color = getScoreColor(uiState.liveScore)
                                )
                            }
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(uiState.messages) { message ->
                    MessageItem(message)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            if (uiState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.currentInput,
                    onValueChange = viewModel::onInputChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type to analyze emotions…") },
                    maxLines = 3,
                    shape = RoundedCornerShape(24.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            viewModel.sendMessage()
                            keyboardController?.hide()
                        }
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                FilledIconButton(
                    onClick = {
                        viewModel.sendMessage()
                        keyboardController?.hide()
                    },
                    enabled = uiState.currentInput.isNotBlank() && !uiState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send"
                    )
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: ChatMessage) {
    val emotionColor = getEmotionColor(message.emotion)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            modifier = Modifier.widthIn(max = 280.dp),
            shape = RoundedCornerShape(16.dp),
            color = if (message.isUser)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.secondaryContainer
        ) {
            Column(modifier = Modifier.padding(12.dp)) {

                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge
                )

                if (message.emotion != null) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 6.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = message.emotion,
                            color = emotionColor,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelSmall
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "${((message.confidence ?: 0f) * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

fun getEmotionColor(emotion: String?): Color {
    return when (emotion?.lowercase()) {
        "happy" -> Color(0xFF4CAF50)
        "sad" -> Color(0xFF2196F3)
        "angry" -> Color(0xFFF44336)
        "fear" -> Color(0xFFFF9800)
        "neutral" -> Color.Gray
        else -> Color.Gray
    }
}

fun getScoreColor(score: Int): Color {
    return when {
        score >= 80 -> Color(0xFF4CAF50) // Green
        score >= 60 -> Color(0xFF8BC34A) // Light Green
        score >= 40 -> Color(0xFFFFC107) // Amber
        score >= 20 -> Color(0xFFFF9800) // Orange
        else -> Color(0xFFF44336) // Red
    }
}
