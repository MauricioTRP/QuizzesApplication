package com.example.quizzesapplication.quizzes.data.remote.service

import com.example.quizzesapplication.quizzes.data.remote.dto.AnswerDto
import com.example.quizzesapplication.quizzes.data.remote.dto.CompletedAtResponseDto
import com.example.quizzesapplication.quizzes.data.remote.dto.QuizDto
import com.example.quizzesapplication.quizzes.data.remote.dto.QuizzesResponseDto
import com.example.quizzesapplication.quizzes.data.remote.dto.SolveResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface QuizzesService {
    @GET("/api/quizzes")
    suspend fun getQuizzes() : QuizzesResponseDto

    @GET("/api/quizzes/{id}")
    suspend fun getQuizById(@Path("id") id: Int) : QuizDto

    @GET("/api/quizzes/completed")
    suspend fun getCompletedQuizzes() : List<CompletedAtResponseDto>

    @POST("/api/quizzes/{id}/solve")
    suspend fun solveQuiz(
        @Path("id") id: Int,
        @Body answer: AnswerDto
    ) : SolveResponseDto
}