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
import retrofit2.HttpException
import java.io.IOException

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
        val answer = inputData.getIntArray(KEY_RESULT)?.toList()
        val entryPoint = EntryPointAccessors.fromApplication(appContext, SyncWorkerEntryPoint::class.java)
        val quizzesService = entryPoint.quizzesService()

        Log.d("SyncWorker", "Starting sync - quizId: $quizId, answer: $answer")

        if (quizId == null || answer == null) {
            Log.e("SyncWorker", "Missing required data - quizId: $quizId, answer: $answer")
            return Result.failure()
        }

        val quizIdInt = quizId.toIntOrNull()
        if (quizIdInt == null) {
            Log.e("SyncWorker", "Invalid quizId format: $quizId")
            return Result.failure()
        }

        return try {
            quizzesService.solveQuiz(quizIdInt, AnswerDto(answer))
            Log.d("SyncWorker", "Successfully synced quiz $quizIdInt")
            Result.success()
        } catch (e: HttpException) {
            when (e.code()) {
                in 400..499 -> {
                    // Client errors - don't retry (bad request, unauthorized, etc.)
                    Log.e("SyncWorker", "Client error ${e.code()} for quiz $quizIdInt: ${e.message}", e)
                    Result.failure()
                }
                in 500..599 -> {
                    // Server errors - retry
                    Log.e("SyncWorker", "Server error ${e.code()} for quiz $quizIdInt: ${e.message}", e)
                    Result.retry()
                }
                else -> {
                    Log.e("SyncWorker", "HTTP error ${e.code()} for quiz $quizIdInt: ${e.message}", e)
                    Result.retry()
                }
            }
        } catch (e: IOException) {
            // Network errors - retry
            Log.e("SyncWorker", "Network error for quiz $quizIdInt: ${e.message}", e)
            Result.retry()
        } catch (e: Exception) {
            // Unexpected errors - don't retry
            Log.e("SyncWorker", "Unexpected error for quiz $quizIdInt: ${e.message}", e)
            Result.failure()
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SyncWorkerEntryPoint {
    fun quizzesService(): QuizzesService
}