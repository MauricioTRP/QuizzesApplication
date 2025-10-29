package com.example.quizzesapplication.quizzes.data.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.quizzesapplication.quizzes.data.room.entity.QuizItemEntity
import com.example.quizzesapplication.quizzes.data.room.entity.QuizOptionsEntity

data class QuizWithOptions(
    @Embedded val quiz: QuizItemEntity,
    @Relation(
        parentColumn = "id", // Parent Table key
        entityColumn = "quiz_id" // "Child" column key
    )
    val options: List<QuizOptionsEntity>
)
