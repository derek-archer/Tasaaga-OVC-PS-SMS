package com.example.tasaagaovcps.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GeminiRepository {
    /** Single-shot response — use for brief insights on a card. */
    suspend fun generateInsight(prompt: String): String

    /** Streaming response — use for the chat bottom sheet. */
    fun streamInsight(prompt: String): Flow<String>
}

class GeminiRepositoryImpl(apiKey: String) : GeminiRepository {

    private val model = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = apiKey,
        generationConfig = generationConfig {
            temperature = 0.4f
            maxOutputTokens = 512
            topP = 0.9f
        },
        systemInstruction = content {
            text(
                """
                You are the AI assistant for Tasaaga OVC Day & Boarding Primary School in
                Sitabaale, Wakiso District, Uganda. The school serves orphans and vulnerable
                children (OVC) under the CBC (Competency-Based Curriculum) for P.1–P.7.

                Rules:
                - Currency is UGX (Uganda Shillings). Always format amounts with commas e.g. UGX 450,000.
                - Classes are P.1, P.2, P.3, P.4, P.5, P.6, P.7.
                - Terms are Term 1 (Feb–May), Term 2 (Jun–Aug), Term 3 (Sep–Dec).
                - Mobile money options are MTN MoMo and Airtel Money.
                - Keep responses concise: maximum 3 bullet points unless the user asks for more.
                - Use a warm, professional tone appropriate for school staff and parents.
                - When data looks unusual, flag it clearly and suggest a practical next step.
                """.trimIndent()
            )
        }
    )

    override suspend fun generateInsight(prompt: String): String {
        return try {
            model.generateContent(prompt).text
                ?: "No insight could be generated. Please try again."
        } catch (e: Exception) {
            "AI analysis temporarily unavailable. Check your internet connection.\n(${e.message})"
        }
    }

    override fun streamInsight(prompt: String): Flow<String> = flow {
        try {
            model.generateContentStream(prompt).collect { chunk ->
                chunk.text?.let { emit(it) }
            }
        } catch (e: Exception) {
            emit("\n\n⚠️ Stream interrupted: ${e.message}")
        }
    }
}
