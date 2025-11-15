package com.example.aitherapist.network

import com.example.aitherapist.network.models.Message
import com.example.aitherapist.network.models.OpenAIRequest
import com.example.aitherapist.network.models.OpenAIResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class OpenAIService {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = false
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
    }
    
    suspend fun getSuggestions(whatDid: String, whatDidNot: String, feelings: String): Result<List<String>> {
        return try {
            val prompt = buildPrompt(whatDid, whatDidNot, feelings)
            
            val request = OpenAIRequest(
                model = OpenAIConfig.MODEL,
                messages = listOf(
                    Message(role = "system", content = "You are a friendly, supportive AI coach and therapist. Be warm, encouraging, and constructive. Provide 2-3 actionable suggestions for improvement. Format your response with each suggestion on a new line starting with a number (1., 2., 3.)."),
                    Message(role = "user", content = prompt)
                ),
                temperature = 0.7,
                max_tokens = 500
            )
            
            val response: OpenAIResponse = client.post("${OpenAIConfig.BASE_URL}chat/completions") {
                header(HttpHeaders.Authorization, "Bearer ${OpenAIConfig.API_KEY}")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                setBody(request)
            }.body()
            
            val suggestionsText = response.choices.firstOrNull()?.message?.content ?: ""
            val suggestions = parseSuggestions(suggestionsText)
            
            Result.success(suggestions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun buildPrompt(whatDid: String, whatDidNot: String, feelings: String): String {
        return """
            Today's Reflection:
            
            What I did: $whatDid
            What I didn't do: $whatDidNot
            How I felt: $feelings
            
            Please analyze this and provide 2-3 friendly, constructive suggestions for tomorrow. Act like a supportive friend or coach. Focus on what could be improved and how to feel better.
        """.trimIndent()
    }
    
    private fun parseSuggestions(text: String): List<String> {
        val numberPattern = Regex("^\\d+[.)]")
        val bulletPattern = Regex("^[-•]")
        return text.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && (numberPattern.containsMatchIn(it) || it.startsWith("-") || it.startsWith("•")) }
            .map { it.replace(numberPattern, "").replace(bulletPattern, "").trim() }
            .filter { it.isNotEmpty() }
            .take(3)
            .ifEmpty { 
                // Fallback: split by newlines if no numbered format
                text.split("\n")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .take(3)
            }
    }
}

