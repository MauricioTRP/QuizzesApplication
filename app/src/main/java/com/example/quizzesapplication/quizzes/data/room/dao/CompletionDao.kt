package com.example.quizzesapplication.quizzes.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.quizzesapplication.quizzes.data.room.entity.CompletionsEntity

@Dao
interface CompletionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completedQuiz: CompletionsEntity)
}