package com.example.quizzesapplication.quizzes.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.quizzesapplication.quizzes.data.room.entity.CompletionsEntity
import com.example.quizzesapplication.quizzes.data.room.entity.QuizItemEntity
import com.example.quizzesapplication.quizzes.data.room.entity.QuizOptionsEntity
import com.example.quizzesapplication.quizzes.data.room.relations.QuizWithOptions
import com.example.quizzesapplication.quizzes.domain.Option


@Dao
interface QuizItemDao {
    @Transaction
    @Query("SELECT * FROM quiz_items")
    suspend fun getQuizWithOptions(): List<QuizWithOptions>

    @Transaction
    @Query("SELECT * FROM quiz_items WHERE id = :id")
    suspend fun getQuizWithOptionsById(id: String): QuizWithOptions

    @Query("SELECT * FROM completions")
    suspend fun getCompletedQuizzes(): List<CompletionsEntity>

    @Transaction
    suspend fun insertAllQuizzes(quizzesWithOption: List<QuizWithOptions>) {
        val quizItems = quizzesWithOption.map { it.quiz }
        val options = quizzesWithOption.map { it.options }.flatten()

        insertQuizzes(quizItems)
        insertOptions(options)
    }

    @Query("DELETE FROM quiz_items")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizzes(quizzes: List<QuizItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOptions(options: List<QuizOptionsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletions(completedQuizItems: List<CompletionsEntity>)
}