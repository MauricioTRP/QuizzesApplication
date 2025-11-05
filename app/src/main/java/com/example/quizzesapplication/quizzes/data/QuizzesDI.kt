package com.example.quizzesapplication.quizzes.data

import android.content.Context
import androidx.room.Room
import com.example.quizzesapplication.AppDatabase
import com.example.quizzesapplication.quizzes.domain.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class QuizDependencies {
    @Binds
    abstract fun bindQuizRepository(quizRepositoryImpl: QuizRepositoryImpl) : QuizRepository

    companion object {
//        @Provides
//        @Singleton
//        fun provideQuizzesService(retrofit: Retrofit) : QuizzesService {
//            return retrofit.create(QuizzesService::class.java)
//        }

        @Singleton
        @Provides
        fun providesDatabase(@ApplicationContext applicationContext: Context) : AppDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "English.db")
            .createFromAsset("database/English_db.db")
            .build()

        @Singleton
        @Provides
        fun providesQuizItemDao(appDatabase: AppDatabase) = appDatabase.quizItemDao()

        @Singleton
        @Provides
        fun providesAnswerDao(appDatabase: AppDatabase) = appDatabase.answerDao()

        @Singleton
        @Provides
        fun providesCompletionDao(appDatabase: AppDatabase) = appDatabase.completionDao()
    }

}