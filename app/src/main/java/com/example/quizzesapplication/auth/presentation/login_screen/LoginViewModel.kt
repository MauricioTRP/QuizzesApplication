package com.example.quizzesapplication.auth.presentation.login_screen

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzesapplication.auth.domain.AuthRepository
import com.example.quizzesapplication.auth.domain.PASSWORD_MIN_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val authRepository: AuthRepository
) : ViewModel() {
    var uiState by mutableStateOf(LoginUIState())
        private set

    /**
     * Function to trigger login request
     */
    fun login() {
        Log.d(TAG, "Login button clicked")
        viewModelScope.launch {
            uiState = uiState.copy(isLoggingIn = true) // Update UI State to show user Login Process

            try {
                val result = authRepository.login(
                    email = uiState.email.text,
                    password = uiState.password.text
                )

                result.fold(
                    onSuccess = { loginResponseDto ->
                        /**
                         * Handle successful operations
                         */
                        Log.d(TAG, loginResponseDto.toString())
                        uiState = uiState.copy(isLoggedIn = true)
                    },
                    onFailure = { failure ->
                        /**
                         * Handle failure operations
                         */
                        Log.e(TAG, failure.message ?: "Unknown failure")
                        uiState = uiState.copy(hasLoginError = true)
                    }
                )
            } catch (e: Error) {
                Log.d("ViewModel Login", e.message.toString())
                uiState = uiState.copy(hasLoginError = true)
            } finally {
                uiState = uiState.copy(isLoggingIn = false)
            }

        }
    }

    /**
     * UI Related Tasks
     */
    fun updatePasswordTextFieldValue(newPassword: TextFieldValue) {
        uiState = uiState.copy(password = newPassword)
        checkCanLogin()
    }

    fun updateEmailTextFieldValue(newEmail: TextFieldValue) {
        uiState = uiState.copy(email = newEmail)
        checkCanLogin()
    }

    fun togglePasswordVisibility() {
        uiState = uiState.copy(isPasswordVisible = !uiState.isPasswordVisible)
    }

    private fun checkCanLogin() {
        uiState = if(Patterns.EMAIL_ADDRESS.matcher(uiState.email.text).matches() && uiState.password.text.length >= PASSWORD_MIN_LENGTH) {
            uiState.copy(canLogin = true)
        } else {
            uiState.copy(canLogin = false)
        }
    }
}

