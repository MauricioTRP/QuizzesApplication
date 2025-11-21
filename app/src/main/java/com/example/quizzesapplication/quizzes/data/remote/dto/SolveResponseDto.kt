package com.example.quizzesapplication.quizzes.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SolveResponseDto(
    val success: String,
    val feedback: String
)
