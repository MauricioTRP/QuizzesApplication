package com.example.quizzesapplication.quizzes.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizzesapplication.quizzes.domain.Option
import com.example.quizzesapplication.quizzes.domain.Quiz
import com.example.quizzesapplication.quizzes.presentation.QuizzesViewModel
import com.example.quizzesapplication.ui.theme.QuizzesApplicationTheme


typealias QuizLinkId = String
@Composable
fun QuestionWithOptionComposable(
    onSubmitAnswer: (QuizLinkId) -> Unit, // Used for navigation
    quizId: String,
    viewModel: QuizzesViewModel,
    modifier: Modifier = Modifier,
) {
    val questionUiState by viewModel.quizUIState.collectAsState()
    var selectedOption by remember { mutableStateOf<Option?>(null) }

    LaunchedEffect(questionUiState) {
        if (questionUiState.currentQuiz == null) {
            viewModel.getQuizById(quizId)
        }
    }

    when {
        // Error Screen and Loading Screens are not implemented yet
        questionUiState.error != null -> Text("Error")
        questionUiState.isLoading -> Text("Loading")
        questionUiState.currentQuiz == null -> Text("Couldn't find quizzes")
        else -> QuestionWithOption(
            quizQuestion = questionUiState.currentQuiz!!,
            selectedOption = selectedOption,
            onSelectAnswer = { selectedOption = it },
            onSubmitAnswer = { option ->
                viewModel.submitQuiz(quizId = quizId, answer = listOf(option.optionId.toInt()))

                // Need a way to get next QuizId
                onSubmitAnswer(quizId)
            },
            modifier = modifier
        )
    }
}

@Composable
private fun QuestionWithOption(
    quizQuestion: Quiz,
    selectedOption: Option?,
    onSelectAnswer: (Option) -> Unit,
    onSubmitAnswer: (Option) -> Unit,
    modifier: Modifier = Modifier
) {
    // Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(modifier = modifier.selectableGroup()) {
        Text(
            text = quizQuestion.text,
            style = MaterialTheme
                .typography
                .headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        quizQuestion.options.forEach { quizOption ->
            val isSelected = selectedOption == quizOption

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp)
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onSelectAnswer(quizOption)
                        },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = {
                        onSelectAnswer(quizOption)
                    }
                )
                Text(
                    text = quizOption.optionText,
                    style = MaterialTheme
                        .typography
                        .bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )

            }
        }

        // Next Button
        Button(
            onClick = { onSubmitAnswer(selectedOption!!) },
            enabled = selectedOption != null,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Submit/Next")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizOptionsPreview() {
    val question = getQuestion()
    val selectedOption = Option(optionText = "2", optionId = "I am going to sleep.")

    QuizzesApplicationTheme {
        QuestionWithOption(
            quizQuestion = question,
            selectedOption = selectedOption,
            onSelectAnswer = {},
            onSubmitAnswer = {},
            modifier = Modifier
        )
    }
}
//
private fun getQuestion() : Quiz {
    return Quiz(
        id = "1",
        text = "How are you?",
        title = "The title of the question?",
        options = listOf(
            Option(optionId = "1", optionText = "I am fine."),
            Option(optionId = "2", optionText = "I am going to sleep."),
            Option(optionId = "3", optionText = "I am hungry."),
            Option(optionId = "4", optionText = "I am tired."),
        )
    )
}