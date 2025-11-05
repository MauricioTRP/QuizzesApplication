package com.example.quizzesapplication.quizzes.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "quiz_options",
    primaryKeys = ["option_ix", "quiz_id"],
    foreignKeys = [ForeignKey(
        entity = QuizItemEntity::class,
        parentColumns = ["id"],
        childColumns = ["quiz_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class QuizOptionsEntity(
    @ColumnInfo(name = "option_text")
    val optionText: String,
    @ColumnInfo(name = "quiz_id")
    val quizId: Int,
    @ColumnInfo(name = "option_ix")
    val optionIX: Int
)
