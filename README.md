# AI Therapist - Your Personal AI Coach & Therapist

A beautiful, colorful Android app that acts as your personal AI coach and therapist. Reflect on your day, log what you did and didn't do, share your feelings, and receive personalized AI-powered suggestions for improvement.
<img src="https://github.com/rajfp/AITherapist/blob/main/Screenshot_20251115_105238.png" width="400" height="800">
<img src="https://github.com/rajfp/AITherapist/blob/main/Screenshot_20251115_113240.png" width="400" height="800">
<img src="https://github.com/rajfp/AITherapist/blob/main/Screenshot_20251115_113248.png" width="400" height="800">

## 🌟 Features

- **Daily Reflection**: Log what you did, what you didn't do, and how you felt
- **AI-Powered Suggestions**: Get 2-3 personalized coaching suggestions from OpenAI
- **Beautiful UI**: Colorful, modern design with mood emojis and smooth animations
- **Friendly Interface**: Warm, uplifting color palette designed for positive psychology
- **Real-time Feedback**: Instant AI analysis and suggestions

## 🎨 Design Philosophy

The app uses psychological design patterns to create a positive, happy user experience:
- Warm, uplifting color palette (oranges, yellows, greens, blues)
- Smooth animations and transitions
- Mood emojis throughout for emotional connection
- Spacious layout to reduce cognitive load
- Friendly, approachable typography

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **HTTP Client**: Ktor
- **Serialization**: Kotlinx Serialization (JSON)
- **State Management**: StateFlow
- **AI Integration**: OpenAI GPT-3.5 Turbo API
- **Coroutines**: For asynchronous operations

## 📋 Prerequisites

- Android Studio Hedgehog or later
- Android SDK 24+ (Android 7.0+)
- OpenAI API Key ([Get one here](https://platform.openai.com/api-keys))

## 🚀 Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd AITherapist
```

### 2. Configure OpenAI API Key

1. Open `app/src/main/java/com/example/aitherapist/network/OpenAIConfig.kt`
2. Replace `YOUR_OPENAI_API_KEY_HERE` with your actual OpenAI API key:

```kotlin
const val API_KEY = "sk-your-actual-api-key-here"
```

**⚠️ Important**: Never commit your API key to version control. Consider using environment variables or a local config file for production.

### 3. Build and Run

#### Using Android Studio:
1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Connect an Android device or start an emulator
4. Click "Run" or press `Shift + F10`

#### Using Command Line:
```bash
./gradlew installDebug
adb shell am start -n com.example.aitherapist/.MainActivity
```

## 📱 Usage

1. **Fill in your reflection**:
   - What I Did Today: Share your accomplishments
   - What I Didn't Do: Note what you planned but couldn't complete
   - How I Felt: Describe your emotions and feelings

2. **Get AI Suggestions**:
   - Tap the "🚀 Get AI Suggestions" button
   - Wait for the AI to analyze your entry
   - Receive 2-3 personalized, constructive suggestions

3. **Review Suggestions**:
   - Read through the AI coach's recommendations
   - Suggestions appear in colorful, animated cards

## 🏗️ Project Structure

```
app/src/main/java/com/example/aitherapist/
├── data/                    # (Removed - no longer using Room)
├── network/
│   ├── OpenAIConfig.kt     # API configuration
│   ├── OpenAIService.kt     # Ktor HTTP client for OpenAI
│   └── models/              # Request/Response data classes
├── ui/
│   ├── components/          # Reusable UI components
│   │   ├── EntryCard.kt
│   │   ├── GradientButton.kt
│   │   └── SuggestionCard.kt
│   ├── screens/
│   │   └── MainScreen.kt   # Main app screen
│   ├── theme/               # Color scheme and theming
│   └── viewmodel/
│       └── MainViewModel.kt  # Business logic and state management
└── MainActivity.kt           # App entry point
```

## 🎯 Key Components

### MainViewModel
- Manages app state using StateFlow
- Handles form input updates
- Coordinates API calls to OpenAI
- Manages loading and error states

### OpenAIService
- Ktor-based HTTP client
- Sends user reflections to OpenAI API
- Parses and formats AI suggestions
- Handles errors gracefully

### UI Components
- **EntryCard**: Colorful input cards with emojis
- **GradientButton**: Animated gradient button with loading states
- **SuggestionCard**: Beautiful cards displaying AI suggestions

## 🔧 Configuration

### Changing the AI Model

Edit `OpenAIConfig.kt`:
```kotlin
const val MODEL = "gpt-4" // or "gpt-3.5-turbo"
```

### Adjusting Colors

Edit `ui/theme/Color.kt` to customize the color palette.

## 📝 Notes

- **No Data Persistence**: Currently, entries are not saved. Data is lost when the app is closed.
- **API Costs**: Each suggestion request uses OpenAI API credits. Monitor your usage.
- **Internet Required**: The app requires an active internet connection to fetch AI suggestions.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Acknowledgments

- OpenAI for the GPT API
- Jetpack Compose team
- Material Design 3

