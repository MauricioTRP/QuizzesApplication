package com.example.quizzesapplication.quizzes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzesapplication.quizzes.domain.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuizzesViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
) : ViewModel() {
    private val _quizUIState = MutableStateFlow(QuizUiState())
    val quizUIState: StateFlow<QuizUiState> = _quizUIState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                quizRepository.sync()
            } catch (e: Exception) {
                e.printStackTrace()
                _quizUIState.value = _quizUIState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun getQuizzes() {
        viewModelScope.launch {
            _quizUIState.value = _quizUIState.value.copy(
                isLoading = true
            )
            quizRepository.getQuizItems().collect { quizzes ->
                _quizUIState.value = _quizUIState.value.copy(
                    quizzes = quizzes,
                    isLoading = false, // change loading state at first quiz being loaded
                    currentQuiz = quizzes.firstOrNull()
                )
            }
        }
    }

    fun getQuizById(quizId: Int) {
        viewModelScope.launch {
            _quizUIState.value = _quizUIState.value.copy(
                currentQuiz = quizUIState.value.quizzes.find { it.id == quizId },
                isLoading = false
            )
        }
    }

    suspend fun submitQuiz(quizId: Int, answer: List<Int>): Boolean {
        val res = viewModelScope.async {
            _quizUIState.value = _quizUIState.value.copy(
                isLoading = true
            )
            val isCorrect = quizRepository.checkAnswer(quizId, answer)
            if (isCorrect) {
                quizRepository.submitCompletion(quizId)
            }
            _quizUIState.value = _quizUIState.value.copy(isLoading = false)
            isCorrect
        }
        return res.await()
    }

    fun updateCurrentQuiz(solvedQuizId: Int) : String {
        val updatedQuizzes = _quizUIState.value.quizzes.filter { it.id != solvedQuizId }
        _quizUIState.value = _quizUIState.value.copy(
            quizzes = updatedQuizzes,
            currentQuiz = updatedQuizzes.firstOrNull()
        )

        return _quizUIState.value.currentQuiz?.id?.toString() ?: "end"
    }
}
