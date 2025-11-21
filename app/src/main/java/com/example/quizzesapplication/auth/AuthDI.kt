package com.example.quizzesapplication.auth

import com.example.quizzesapplication.auth.data.AuthRepositoryImpl
import com.example.quizzesapplication.auth.data.network.AuthApiService
import com.example.quizzesapplication.auth.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDependencies {
    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl) : AuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideApiAuthService(retrofit: Retrofit) : AuthApiService {
            return retrofit.create(AuthApiService::class.java)
        }
    }
}