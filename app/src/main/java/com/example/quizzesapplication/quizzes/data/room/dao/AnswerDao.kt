package com.example.quizzesapplication.quizzes.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.quizzesapplication.quizzes.data.room.entity.AnswersEntity
import com.example.quizzesapplication.quizzes.data.room.relations.QuizWithOptions

@Dao
interface AnswerDao {
    @Transaction
    @Query("SELECT option_ix FROM answers WHERE quiz_id = :quizId")
    suspend fun getCorrectOptionsIxsByQuizId(quizId: Int): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: List<AnswersEntity>)
}
