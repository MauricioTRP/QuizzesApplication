package com.example.quizzesapplication.common.data.network

import com.example.quizzesapplication.common.domain.TokenProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


/**
 * [AuthorizationInterceptor] is responsible to add `Bearer <auth_token>` on
 * every request.
 *
 * It uses [TokenProvider] to get an stored token and add header
 *
 * @property tokenProvider an implementation of [TokenProvider]
 */
class AuthorizationInterceptor @Inject constructor (
    private val tokenProvider: TokenProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val tokens = runBlocking { tokenProvider.getToken() }
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${tokens?.accessToken}")
            .build()

        return chain.proceed(request)
    }
}