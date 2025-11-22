package com.example.quizzesapplication.auth.presentation.login_screen

import androidx.compose.ui.text.input.TextFieldValue

data class LoginUIState(
    val email: TextFieldValue = TextFieldValue(),
    val password: TextFieldValue = TextFieldValue(),
    val isPasswordVisible: Boolean = false,
    val canLogin: Boolean = false,
    val hasLoginError: Boolean = false,
    val isLoggingIn: Boolean = false,
    val isLoggedIn: Boolean = false
)

