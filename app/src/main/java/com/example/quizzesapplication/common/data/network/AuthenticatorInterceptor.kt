package com.example.quizzesapplication.common.data.network

import android.util.Log
import com.example.quizzesapplication.auth.data.network.AuthApiService
import com.example.quizzesapplication.common.data.network.dto.RefreshTokenRequestDto
import com.example.quizzesapplication.common.domain.AuthTokens
import com.example.quizzesapplication.common.domain.TokenProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

/**
 * [AuthenticatorInterceptor] is responsible to handle `401 HTTP Code` responses
 * of, in this case, [AuthApiService]. is a
 *
 * It updates stored [AuthTokens] via [TokenProvider]
 *
 * @property tokenProvider an implementation of [TokenProvider]
 * @property apiService a provider [Provider] of [AuthApiService]
 *
 * [Provider] is used to break [Circular Dependencies](https://docs.oracle.com/javaee/6/api/javax/inject/Provider.html).
 * Allowing to get an instance of [AuthApiService] in a lazy way
 *
 *
 */
class AuthenticatorInterceptor @Inject constructor (
    private val tokenProvider: TokenProvider,
    private val apiService: Provider<AuthApiService>
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val service = apiService.get()
        val authTokens = runBlocking { tokenProvider.getToken() }

        val refreshTokenResponse = runBlocking {
            try {
                if(authTokens?.refreshToken.isNullOrBlank()) {
                    throw Exception("Refresh token is null or blank")
                }
                service.refreshToken(RefreshTokenRequestDto(refreshToken = authTokens.refreshToken))
            } catch (e: Exception) {
                Log.d(AUTH_INTERCEPTOR_TAG, e.toString())
                null
            }
        }

        if (refreshTokenResponse != null && refreshTokenResponse.isSuccessful) {
            val updatedToken = refreshTokenResponse.body()
            if (updatedToken != null) {
                runBlocking {
                    tokenProvider.setToken(authTokens = AuthTokens(
                        accessToken = updatedToken.accessToken,
                        refreshToken = updatedToken.refreshToken
                    )
                    )
                }

                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${updatedToken.accessToken}")
                    .build()
            } else {
                // Response body null handling
                return null
            }
        } else {
            // Handle network error
            return null // Failed authentication
        }
    }

    companion object {
        private const val AUTH_INTERCEPTOR_TAG = "AuthInterceptor"
    }
}
