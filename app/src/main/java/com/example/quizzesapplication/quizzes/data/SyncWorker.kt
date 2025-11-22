package com.example.quizzesapplication.quizzes.data

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quizzesapplication.quizzes.data.remote.dto.AnswerDto
import com.example.quizzesapplication.quizzes.data.remote.service.QuizzesService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 *
 * [Check entry point explanation at Youtube](https://www.youtube.com/watch?v=-6WXLIOAO7E)
 */
@HiltWorker
class SyncWorker @AssistedInject constructor (
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val KEY_QUIZ_ID = "KEY_QUIZ_ID"
        const val KEY_RESULT = "KEY_RESULT"
    }



    override suspend fun doWork(): Result {
        val quizId = inputData.getString(KEY_QUIZ_ID)
        val answer = inputData.getIntArray(KEY_RESULT) ?.toList()
        val entryPoint = EntryPointAccessors.fromApplication(appContext, SyncWorkerEntryPoint::class.java)
        val quizzesService = entryPoint.quizzesService()

        print("quizId: $quizId, answer: $answer")

        if (quizId == null || answer == null) {
            return Result.failure()
        }

        return try {
            quizzesService.solveQuiz(quizId.toInt(), AnswerDto(answer))

            Result.success()
        } catch (e: Exception) {
            Log.e("Sync Worker", "Error while enqueuing task")
            e.printStackTrace()
            Result.retry() // HTTPClient will handle backoff strategy
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SyncWorkerEntryPoint {
    fun quizzesService(): QuizzesService
}