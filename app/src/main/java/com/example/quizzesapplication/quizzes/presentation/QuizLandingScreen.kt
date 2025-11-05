package com.example.quizzesapplication.quizzes.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuizLandingScreen(
    onStartQuiz: (String) -> Unit,
    showSnackbar: (String) -> Unit,
    quizzesViewModel: QuizzesViewModel,
    modifier: Modifier = Modifier
) {
    val quizUiState by quizzesViewModel.quizUIState.collectAsState()
    quizzesViewModel.getQuizzes()

    Column(modifier = modifier) {
        Text(
            text = "Welcome to the Quiz App!",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text("Click the button below to start the quiz")

        Button(
            onClick = {
                quizUiState.quizzes.firstOrNull()?.let {
                    onStartQuiz(it.id.toString())
                }
            },
            enabled = quizUiState.quizzes.isNotEmpty(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Start Quiz")
        }
    }

    if (quizUiState.error != null) {
        showSnackbar(quizUiState.error ?: "Unknown Error Occurred")
    }
}
