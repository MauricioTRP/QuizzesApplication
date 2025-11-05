package com.example.quizzesapplication.quizzes.domain

import kotlinx.coroutines.flow.Flow

/**
 * QuizRepository interface
 *
 * There are several ways to interpreter
 * the DOMAIN Layer.
 *
 * In this case, the business logic is handled
 * in DOMAIN layer
 */
interface QuizRepository {
    suspend fun getQuizItems(): Flow<List<Quiz>>
    suspend fun getQuizItemById(id: String): Quiz
    suspend fun getCompletedQuizzes(): List<QuizCompleted>
    suspend fun checkAnswer(quizId: Int, answer: List<Int>): Boolean
    suspend fun submitCompletion(quizId: Int)
    suspend fun sync() : Boolean
}