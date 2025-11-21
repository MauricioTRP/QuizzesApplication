package com.example.quizzesapplication.common.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponseDto(
    val accessToken: String,
    val refreshToken: String
)