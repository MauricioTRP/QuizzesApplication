package com.example.quizzesapplication.auth.data.network

import com.example.quizzesapplication.auth.data.network.dto.LoginRequestDto
import com.example.quizzesapplication.auth.data.network.dto.LoginResponseDto
import com.example.quizzesapplication.auth.data.network.dto.RegisterRequestDto
import com.example.quizzesapplication.auth.data.network.dto.RegisterResponseDto
import com.example.quizzesapplication.common.data.network.dto.RefreshTokenRequestDto
import com.example.quizzesapplication.common.data.network.dto.RefreshTokenResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/login")
    suspend fun login(
        @Body
        loginRequestDto: LoginRequestDto
    ) : Response<LoginResponseDto>

    @POST("/api/register")
    suspend fun register(
        @Body
        registerRequestDto: RegisterRequestDto
    ) : Response<RegisterResponseDto>

    @POST("/api/refresh-token")
    suspend fun refreshToken(
        @Body
        refreshToken: RefreshTokenRequestDto
    ) : Response<RefreshTokenResponseDto>
}