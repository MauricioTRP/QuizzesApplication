package com.example.quizzesapplication

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quizzesapplication.quizzes.data.room.RoomConverters
import com.example.quizzesapplication.quizzes.data.room.dao.AnswerDao
import com.example.quizzesapplication.quizzes.data.room.dao.QuizItemDao
import com.example.quizzesapplication.quizzes.data.room.entity.AnswersEntity
import com.example.quizzesapplication.quizzes.data.room.entity.CompletionsEntity
import com.example.quizzesapplication.quizzes.data.room.entity.QuizItemEntity
import com.example.quizzesapplication.quizzes.data.room.entity.QuizOptionsEntity


@Database(
    entities = [
        AnswersEntity::class,
        CompletionsEntity::class,
        QuizItemEntity::class,
        QuizOptionsEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun answerDao() : AnswerDao
    abstract fun quizItemDao() : QuizItemDao

}
