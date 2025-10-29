package com.example.quizzesapplication.quizzes.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_items")
data class QuizItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    val text: String,
    val title: String
)
