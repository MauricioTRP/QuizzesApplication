package com.example.quizzesapplication.quizzes.data.remote.mappers

import com.example.quizzesapplication.quizzes.data.remote.dto.QuizDto
import com.example.quizzesapplication.quizzes.domain.Option
import com.example.quizzesapplication.quizzes.domain.Quiz

fun QuizDto.toDomain() : Quiz {
    return Quiz(
        id = this.id,
        title = this.title,
        text = this.text,
        options = this.options.mapIndexed { index, optionText ->
            Option(
                optionText = optionText,
                optionId = index.toString()
            )
        }
    )
}