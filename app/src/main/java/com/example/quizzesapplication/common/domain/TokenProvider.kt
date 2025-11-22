package com.example.quizzesapplication.common.domain

/**
 * Manages asynchronous storage and retrieval of [AuthTokens].
 *
 * Implementations of this interface are responsible for securely handling authentication tokens.
 */
interface TokenProvider {
    /**
     * Retrieves the currently stored authentication token.
     *
     * @return the stored [AuthTokens], or null if no token are currently stored
     */
    suspend fun getToken(): AuthTokens?

    /**
     * Stores the provided authentication tokens.
     * This will typically overwrite any existing tokens
     *
     * @param authTokens the [AuthTokens] instance containing the tokens to be stored.
     */
    suspend fun setToken(authTokens: AuthTokens)

    /**
     * Clears the stored authentication token.
     */
    suspend fun clearToken()
}