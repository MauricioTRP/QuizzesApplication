package com.example.quizzesapplication.quizzes.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * [AnswerDto] represents a single question answered
 *
 * Example Payload:
 *
 * ```text
 * # POST /api/quizzes/{id}/solve
 *
 * {
 *   "answer": [1, 2, 3]
 * }
 * ```
 */
@Serializable
data class AnswerDto(
    val answer: List<Int>
)
