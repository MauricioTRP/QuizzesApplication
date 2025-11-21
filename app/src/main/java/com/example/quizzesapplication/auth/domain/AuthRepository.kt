package com.example.quizzesapplication.auth.domain

import com.example.quizzesapplication.auth.data.network.dto.LoginResponseDto
import com.example.quizzesapplication.auth.data.network.dto.RegisterResponseDto

interface AuthRepository {
    suspend fun login(email: String, password: String) : Result<LoginResponseDto>
    suspend fun register(email: String, password: String, acceptedTerms: Boolean) : Result<RegisterResponseDto>
    suspend fun restoreSession() : Result<Unit>
}