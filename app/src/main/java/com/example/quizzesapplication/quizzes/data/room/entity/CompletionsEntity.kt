package com.example.quizzesapplication.quizzes.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * # Completions
 *
 * userId will be handled by core data userId provided by tokens?
 */
@Entity(tableName = "completions")
data class CompletionsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "quiz_item_id")
    val quizItemId: Int,
    @ColumnInfo(name = "completed_at")
    val completedAt: Date
)
