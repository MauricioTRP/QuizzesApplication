package com.example.quizzesapplication.quizzes.presentation

import com.example.quizzesapplication.quizzes.domain.Quiz

data class QuizUiState(
    val isLoading: Boolean = true,
    val quizzes: List<Quiz> = emptyList(),
    val currentQuiz: Quiz? = null,
    val error: String? = null,
)
