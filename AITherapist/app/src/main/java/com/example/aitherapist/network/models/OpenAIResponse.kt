package com.example.aitherapist.network.models

import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResponse(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>
)

@Serializable
data class Choice(
    val index: Int,
    val message: Message,
    val finish_reason: String
)

