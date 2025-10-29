package com.example.quizzesapplication.quizzes.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answers")
data class AnswersEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "quiz_id")
    val quizId: Int,
    @ColumnInfo(name = "option_ix")
    val optionIX: Int
)
