package com.example.quizzesapplication.quizzes.domain

import java.util.Date

data class Quiz(
    val id: Int,
    val text: String,
    val title: String,
    val options: List<Option>
)

data class QuizCompleted(
    val id: String,
    val completedAt: Date
)