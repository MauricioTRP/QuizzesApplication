package com.example.quizzesapplication.common.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthDataDto (
    val token: String,
    val refreshToken: String
)