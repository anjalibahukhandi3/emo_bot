# Emotion Monitor Android App

## 📱 Project Overview
**Emotion Monitor** is a native Android application built with Kotlin and Jetpack Compose that provides real-time emotion analysis of user chat messages. It utilizes a Clean Architecture approach with MVVM to ensure scalability and testability.

## 🛠 Tech Stack
- **Language**: Kotlin
- **UI Toolkit**: Jetpack Compose (Material 3)
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Networking**: Retrofit 2 + OkHttp 3 (with Mock capabilities)
- **Concurrency**: Kotlin Coroutines + Flow
- **State Management**: StateFlow
- **Minimum SDK**: 24 (Android 7.0)

## 🏗 Architecture
The app follows the recommended Android App Architecture:
1.  **UI Layer**: `ChatScreen` (Composable) observes `UiState` from `EmotionViewModel`.
2.  **Domain/Business Layer**: `EmotionViewModel` handles business logic and communicates with the Repository.
3.  **Data Layer**: `EmotionRepository` abstracts the data source. `RetrofitClient` provides the network service (currently configured with a `MockEmotionApiService` for demonstration).

## 🚀 How It Works
1.  User types a message in the chat input.
2.  The app sends the text to the Analysis Service (simulated locally in Demo mode).
3.  The service detects one of 5 emotions: **Happy, Sad, Angry, Fear, Neutral**.
4.  The UI updates in real-time to show the detected emotion with a confidence score and color-coded indicator.

## 📦 How to Build & Run
1.  **Clone the repository**.
2.  **Open in Android Studio** 
3.  **Sync Gradle** to download dependencies.
4.  **Run** on an Emulator or Physical Device.
    *   *Note: The app is currently set to use a Mock API. To use a real backend, uncomment the Retrofit builder in `RetrofitClient.kt` and provide a valid `BASE_URL`.*

