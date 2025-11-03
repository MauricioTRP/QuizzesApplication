package com.example.quizzesapplication.quizzes.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizzesapplication.quizzes.domain.Quiz

@Composable
fun QuizDetailScreen(
    quizId: Int,
    quizzesViewModel: QuizzesViewModel
) {
    LaunchedEffect(key1 = quizId) {
        quizzesViewModel.updateCurrentQuiz(quizId)
    }
    val quizUiState by quizzesViewModel.quizUIState.collectAsState()

    if (quizUiState.isLoading) {
        CircularProgressIndicator()
    } else {
        quizUiState.currentQuiz?.let {
            QuizDetailContent(it)
        } ?: Text(text = "Quiz not found")
    }
}

@Composable
fun QuizDetailContent(quizItem: Quiz) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = quizItem.title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}