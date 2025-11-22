package com.example.quizzesapplication.auth.presentation.register_screen

import androidx.compose.ui.text.input.TextFieldValue
import com.example.quizzesapplication.auth.domain.PasswordValidationState

data class RegisterUIState(
    val email: TextFieldValue = TextFieldValue(),
    val isEmailValid: Boolean = false,
    val password: TextFieldValue = TextFieldValue(),
    val acceptedTOS: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val isRegistering: Boolean = false,
    val canRegister: Boolean = false,
    val hasRegisterError: Boolean = false,
    val isLoggedIn: Boolean = false
)
