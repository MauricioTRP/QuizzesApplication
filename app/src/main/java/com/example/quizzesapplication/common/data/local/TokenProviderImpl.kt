package com.example.quizzesapplication.common.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.example.quizzesapplication.common.domain.AuthTokens
import com.example.quizzesapplication.common.domain.TokenProvider
import com.example.quizzesapplication.proto.AuthTokensProto
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Token Provider implementation
 *
 * It'll work as the main class to Write and Read data from the Proto Datastore <AuthToken>
 */
class TokenProviderImpl @Inject constructor (
    private val tokenStore: DataStore<AuthTokensProto> // Same name as `Message` in `proto/auth_tokens.proto`
) : TokenProvider {
    override suspend fun getToken() : AuthTokens {
        val sessionToken = try {
            tokenStore.data.first()
        } catch (_: IOException) {
            AuthTokensProto.getDefaultInstance()
        }
        return AuthTokens(
            accessToken = sessionToken.accessToken,
            refreshToken = sessionToken.refreshToken
        )
    }

    override suspend fun setToken(authTokens: AuthTokens) {
        tokenStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setAccessToken(authTokens.accessToken)
                .setRefreshToken(authTokens.refreshToken)
                .build()
        }
    }
}