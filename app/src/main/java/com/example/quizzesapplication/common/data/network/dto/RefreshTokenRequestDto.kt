package com.example.quizzesapplication.common.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequestDto(
    val refreshToken: String
)