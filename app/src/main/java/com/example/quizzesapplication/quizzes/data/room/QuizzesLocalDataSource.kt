package com.example.quizzesapplication.quizzes.data.room


import com.example.quizzesapplication.quizzes.data.room.dao.AnswerDao
import com.example.quizzesapplication.quizzes.data.room.dao.CompletionDao
import com.example.quizzesapplication.quizzes.data.room.dao.QuizItemDao
import com.example.quizzesapplication.quizzes.data.room.entity.AnswersEntity
import com.example.quizzesapplication.quizzes.data.room.entity.CompletionsEntity
import com.example.quizzesapplication.quizzes.data.room.mappers.toQuizWithOptions
import com.example.quizzesapplication.quizzes.data.room.mappers.toDomain
import java.util.Date
import javax.inject.Inject
import com.example.quizzesapplication.quizzes.domain.Quiz
import com.example.quizzesapplication.quizzes.domain.QuizCompleted

typealias QuizId = Int

class QuizzesLocalDataSource @Inject constructor (
    private val quizItemDao: QuizItemDao,
    private val answerDao: AnswerDao,
    private val completionDao: CompletionDao
) {
    suspend fun getCorrectOptionsIxsByQuizId(quizId: Int): List<Int> {
        return answerDao.getCorrectOptionsIxsByQuizId(quizId)
    }

    suspend fun getQuizItems() : List<Quiz> {
        return quizItemDao.getQuizWithOptions().map { it.toDomain() }
    }

    suspend fun getQuizById(id: String) : Quiz {
        return quizItemDao.getQuizWithOptionsById(id).toDomain()
    }

    suspend fun getCompletedQuizzes() : List<QuizCompleted> {
        return quizItemDao.getCompletedQuizzes().map { it.toDomain() }
    }

    suspend fun insertCompletion(quizId: Int) {
        completionDao.insertCompletion(CompletionsEntity(
            quizItemId = quizId,
            completedAt = Date()
        ))
    }

    suspend fun insertAnswers(answers: List<AnswersEntity>) {
        answerDao.insertAnswer(answers)
    }

    suspend fun insertQuizzes(quizzes: List<Quiz>) {
        quizItemDao.insertAllQuizzes(quizzes.map { it.toQuizWithOptions() })
    }

    suspend fun deleteAll() {
        quizItemDao.deleteAll()
    }
}
