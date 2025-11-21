package com.example.quizzesapplication.auth.data.network.dto

import com.example.quizzesapplication.common.data.network.dto.UserDataDto
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDataDto? = null // Optional user data
)