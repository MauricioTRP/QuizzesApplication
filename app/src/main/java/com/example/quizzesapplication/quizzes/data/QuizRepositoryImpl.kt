package com.example.quizzesapplication.quizzes.data

import android.content.Context
import android.util.Log
import com.example.quizzesapplication.quizzes.data.room.QuizzesLocalDataSource
import com.example.quizzesapplication.quizzes.data.room.mappers.toAnswerEntity
import com.example.quizzesapplication.quizzes.domain.Quiz
import com.example.quizzesapplication.quizzes.domain.QuizRepository
import com.example.quizzesapplication.quizzes.domain.QuizCompleted
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


/**
 * An implementation of [QuizRepository]
 *
 * This repository is responsible for handling data operations for quizzes.
 *
 * @property quizzesLocalDataSource the local data source for quizzes.
 * @property quizzesRemoteDataSource the remote data source for quizzes.
 */
class QuizRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val quizzesLocalDataSource: QuizzesLocalDataSource,
    // private val quizzesRemoteDataSource: QuizzesService
) : QuizRepository {

    /**
     * Retrieves a list of questions using an offline-first approach
     * Returns a flow that emits local data first, then updates with remote data when available
     * and emits again
     *
     */
    override suspend fun getQuizItems(): Flow<List<Quiz>> = flow {
        val cachedQuizzes = quizzesLocalDataSource.getQuizItems()
        emit(cachedQuizzes)
    }

    override suspend fun getQuizItemById(id: String): Quiz {
        return quizzesLocalDataSource.getQuizById(id)
    }

    override suspend fun getCompletedQuizzes(): List<QuizCompleted> {
        return quizzesLocalDataSource.getCompletedQuizzes()
    }

    override suspend fun submitAnswer(
        quizId: String,
        answer: List<Int>
    ) {
        val answers = answer.map { (quizId to it).toAnswerEntity() }
        quizzesLocalDataSource.insertAnswers(answers)

        // queueSubmissionSync(quizId, answer)
    }

    override suspend fun sync(): Boolean {
        // TODO("Not yet implemented")
        return true
    }

    // Need to implement in later phases Remote Sync
}
