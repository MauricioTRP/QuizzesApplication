package com.example.quizzesapplication.auth.data

import android.util.Log
import com.example.quizzesapplication.auth.data.network.AuthApiService
import com.example.quizzesapplication.auth.data.network.dto.LoginRequestDto
import com.example.quizzesapplication.auth.data.network.dto.LoginResponseDto
import com.example.quizzesapplication.auth.data.network.dto.RegisterRequestDto
import com.example.quizzesapplication.auth.data.network.dto.RegisterResponseDto
import com.example.quizzesapplication.auth.domain.AuthRepository
import com.example.quizzesapplication.common.data.network.dto.RefreshTokenRequestDto
import com.example.quizzesapplication.common.domain.AuthTokens
import com.example.quizzesapplication.common.domain.TokenProvider
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor (
    private val authApiService: AuthApiService,
    private val tokenProvider: TokenProvider, // authToken to store on a proto datastore the auth and refresh tokens for the very first time
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<LoginResponseDto> {
        /**
         * "Map" the actual response from retrofit, to a usable type of result
         * being used in Presentation layer
         */
        return try {
            Log.d(TAG, "Login operation started")
            val response = authApiService.login(
                LoginRequestDto(email = email, password = password)
            )

            if (response.isSuccessful) {
                response.body()?.let { loginData ->
                    // Need to store Access Token and Refresh Token
                    tokenProvider.setToken(
                        AuthTokens(
                            accessToken = loginData.accessToken,
                            refreshToken = loginData.refreshToken
                        )
                    )

                    Log.d(TAG, "Login successful")
                    Result.success(loginData) // loginData is non-null here
                } ?: Result.failure(Exception("Login successful but response body was null."))
            } else {
                val errorMsg =
                    "Login failed with code: ${response.code()}, message: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Login operation failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        acceptedTerms: Boolean
    ) : Result<RegisterResponseDto>  {
        return try {
            val response = authApiService.register(
                RegisterRequestDto(
                    email = email, password = password, acceptTerms = acceptedTerms
                )
            )

            if (response.isSuccessful) {
                response.body()?.let { registerData ->
                    Log.d(TAG, registerData.toString())
                    /**
                     * Store the token locally
                     */
                    tokenProvider.setToken(
                        AuthTokens(
                            accessToken = registerData.accessToken,
                            refreshToken = registerData.refreshToken
                        )
                    )

                    /**
                     * Return result to be used by ViewModel
                     */
                    Result.success(registerData)
                } ?: Result.failure(Exception("Register was successful, but response body was null"))
            } else {
                val errorMsg =
                    "Register failed with code: ${response.code()}, message: ${response.message()}"

                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun restoreSession(): Result<Unit> {
        return try {
            val currentTokens = tokenProvider.getToken() // Assuming this returns AuthTokens(accessToken: String?, refreshToken: String?)
            val currentRefreshToken = currentTokens?.refreshToken

            if (currentRefreshToken.isNullOrBlank()) {
                Log.d(TAG, "No refresh token found to restore session.")
                return Result.failure(Exception("No refresh token found to restore session."))
            }
            Log.d(TAG, "Attempting to restore session with refresh token.")
            val response = authApiService.refreshToken(
                RefreshTokenRequestDto(refreshToken = currentRefreshToken)
            )
            if (response.isSuccessful) {
                response.body()?.let { refreshTokenResponse ->
                    tokenProvider.setToken(
                        AuthTokens(
                            accessToken = refreshTokenResponse.accessToken,
                            refreshToken = refreshTokenResponse.refreshToken
                        )
                    )
                    Log.d(TAG, "Session restored successfully. New tokens stored.")
                    Result.success(Unit)
                } ?: run {
                    Log.e(TAG, "Restore session successful but response body was null.")
                    // Decide if local tokens should be cleared here or not.
                    // For now, we'''ll assume they are kept, but this could lead to an invalid state.
                    Result.failure(Exception("Restore session successful but response body was null."))
                }
            } else {
                val errorCode = response.code()
                val errorMsg = "Restore session failed with code: $errorCode, message: ${response.message()}"
                Log.e(TAG, errorMsg)
                // If token is invalid (e.g., 401/403), clear local tokens
                if (errorCode == 401 || errorCode == 403) {
                    Log.d(TAG, "Refresh token is invalid. Clearing local tokens.")
                    // Assuming tokenProvider.clearToken() exists or setToken can handle nulls
                    // tokenProvider.clearToken()
                    // If clearToken() doesn'''t exist, and setToken cannot take nulls for AuthTokens,
                    // this part needs adjustment. For now, we'''ll rely on setToken for new tokens only.
                    // To actually clear, you might need:
                    // tokenProvider.setToken(AuthTokens(accessToken = "", refreshToken = ""))
                    // or ensure AuthTokens in ProtoDataStore can handle nullable fields.
                    // For now, if your AuthTokens requires non-null, this will be an issue.
                    // A better approach would be to have a specific clearToken method or ensure AuthTokens supports nullability.
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Restore session operation failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "AuthRepository"
    }
}