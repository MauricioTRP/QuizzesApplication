package com.example.quizzesapplication.auth.data.network.dto

import com.example.quizzesapplication.common.data.network.dto.UserDataDto
import kotlinx.serialization.Serializable

/**
 * DTO for Login Response
 *
 * this depends exclusively on how the Backend is structured
 * So it may change on the project lab
 */

@Serializable
data class RegisterResponseDto(
    val user: UserDataDto? = null,
    val accessToken: String,
    val refreshToken: String
)