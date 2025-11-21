package com.example.quizzesapplication.quizzes.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuizDto(
    val id: Int,
    val title: String,
    val text: String,
    val options: List<String>
)

@Serializable
data class CompletedAtDataDto(
    val id: Int,
    val completedAt: String
)

/**
 * [QuizzesResponseDto] represents a list of [QuizDto]
 *
 * ```text
 * # GET /api/quizzes
 *
 * {
 *   "content": [
 *     {
 *       "id": 1,
 *       "title": "Name the song",
 *       "text": "Which song is this?",
 *       "options": [
 *         "La bamba",
 *         "Largame la mánga",
 *         "Equilibrio Espiritual"
 *       ]
 *     },
 *     ...
 *   ]
 * }
 * ```
 */
@Serializable
data class QuizzesResponseDto(
    val content: List<QuizDto>
)

/**
 * [CompletedAtResponseDto] represents a list of [CompletedAtDataDto]
 *
 * ```text
 * # GET /api/quizzes/completed
 *
 * {
 *     "content": [
 *         {
 *             "id": 1,
 *             "completedAt": "2025-10-10T02:32:35.99891"
 *         },
 *         {
 *             "id": 2,
 *             "completedAt": "2025-10-10T02:31:30.356742"
 *         }
 *     ],
 * }
 *
 * ```
 */
@Serializable
data class CompletedAtResponseDto(
    val content: List<CompletedAtDataDto>
)
