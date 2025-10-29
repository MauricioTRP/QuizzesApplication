package com.example.quizzesapplication.quizzes.data.room.mappers

import com.example.quizzesapplication.quizzes.data.room.entity.CompletionsEntity
import com.example.quizzesapplication.quizzes.data.room.entity.QuizItemEntity
import com.example.quizzesapplication.quizzes.data.room.entity.QuizOptionsEntity
import com.example.quizzesapplication.quizzes.data.room.relations.QuizWithOptions
import com.example.quizzesapplication.quizzes.domain.Option
import com.example.quizzesapplication.quizzes.domain.Quiz
import com.example.quizzesapplication.quizzes.domain.QuizCompleted
import kotlin.toString


fun QuizWithOptions.toDomain() : Quiz {
    val quizEntity = this.quiz
    val optionsEntity = this.options

    return Quiz(
        id = quizEntity.id.toString(),
        text = quizEntity.text,
        title = quizEntity.title,
        options = optionsEntity.map { it.toDomain() }
    )
}

fun QuizOptionsEntity.toDomain() : Option {
    return Option(
        optionText = this.optionText,
        optionId = this.optionIX.toString()
    )
}

fun CompletionsEntity.toDomain() : QuizCompleted {
    return QuizCompleted(
        id = this.id.toString(),
        completedAt = this.completedAt
    )
}

fun QuizCompleted.toCompletionsEntity(quizId: Int) : CompletionsEntity {
    return CompletionsEntity(
        id = this.id.toInt(),
        completedAt = this.completedAt,
        quizItemId = quizId
    )
}

fun Quiz.toQuizWithOptions() : QuizWithOptions {
    return QuizWithOptions(
        quiz = this.toQuizItemEntity(),
        options = this.options.mapIndexed { index, option ->
            option.toQuizOptionsEntity(this.id.toInt())
        }
    )
}

fun Quiz.toQuizItemEntity() : QuizItemEntity {
    return QuizItemEntity(
        id = this.id.toInt(),
        text = this.text,
        title = this.title
    )
}

fun Option.toQuizOptionsEntity(quizId: Int) : QuizOptionsEntity {
    return QuizOptionsEntity(
        optionText = this.optionText,
        quizId = quizId,
        optionIX = this.optionId.toInt()
    )
}