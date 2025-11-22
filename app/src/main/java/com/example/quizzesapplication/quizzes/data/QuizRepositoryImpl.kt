package com.example.quizzesapplication.quizzes.data

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import com.example.quizzesapplication.quizzes.data.remote.mappers.toDomain
import com.example.quizzesapplication.quizzes.data.remote.service.QuizzesService
import com.example.quizzesapplication.quizzes.data.room.QuizzesLocalDataSource
import com.example.quizzesapplication.quizzes.data.room.mappers.toAnswerEntity
import com.example.quizzesapplication.quizzes.domain.Quiz
import com.example.quizzesapplication.quizzes.domain.QuizRepository
import com.example.quizzesapplication.quizzes.domain.QuizCompleted
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
    private val quizzesRemoteDataSource: QuizzesService
) : QuizRepository {

    // Mutex to prevent concurrent sync operations
    private val syncMutex = Mutex()

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

    override suspend fun checkAnswer(quizId: Int, answer: List<Int>): Boolean {
        return quizzesLocalDataSource.getCorrectOptionsIxsByQuizId(quizId).sorted() == answer.sorted()
    }

    override suspend fun submitCompletion(quizId: Int, answer: List<Int>?) {
        quizzesLocalDataSource.insertCompletion(quizId = quizId)
        
        // Queue sync worker to send answer to server if provided
        answer?.let {
            queueSubmissionSync(quizId.toString(), it)
        }
    }

    override suspend fun sync(): Boolean = syncMutex.withLock {
        Log.d(TAG, "sync called")
        return try {
            val remoteQuizzes = quizzesRemoteDataSource.getQuizzes().content.map { it.toDomain() }
            // Use replaceAllQuizzes which uses a transaction to prevent data loss
            // Transaction ensures atomicity: if insert fails, delete is rolled back
            quizzesLocalDataSource.replaceAllQuizzes(remoteQuizzes)
            Log.d(TAG, "Sync completed successfully - ${remoteQuizzes.size} quizzes synced")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Sync failed", e)
            false
        }
    }

    private fun queueSubmissionSync(quizId: String, answer: List<Int>) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putString(SyncWorker.KEY_QUIZ_ID, quizId)
            .putIntArray(SyncWorker.KEY_RESULT, answer.toIntArray())
            .build()

        val submissionWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10,
                timeUnit = TimeUnit.SECONDS
            )
            .addTag("quiz_sync")
            .addTag("quiz_$quizId")
            .build()

        // Use unique work to prevent duplicate submissions for the same quiz
        val uniqueWorkName = "sync_quiz_$quizId"
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                uniqueWorkName,
                ExistingWorkPolicy.REPLACE,
                submissionWorkRequest
            )
        
        Log.d(TAG, "Enqueued sync work for quiz $quizId")
    }

    companion object {
        const val TAG = "QuizRepository"
    }
}
