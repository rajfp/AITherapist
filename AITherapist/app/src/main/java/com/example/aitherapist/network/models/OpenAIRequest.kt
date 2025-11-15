package com.example.aitherapist.network.models

import kotlinx.serialization.Serializable

@Serializable
data class OpenAIRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 500
)

@Serializable
data class Message(
    val role: String,
    val content: String
)

