package com.example.quizzesapplication.common.data.network

import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

/**
 * [HttpClientFactory] creates an HTTP Client to be used by retrofit
 * It handles authentication and authorization process to an specific
 * service
 *
 * @param authenticator is responsible for refreshing auth tokens
 * @param authorization is responsible of adding Bearer token on each request
 * @param exponentialBackoffInterceptor is responsible of retrying failed requests
 */
class HttpClientFactory @Inject constructor (
    private val authenticator: Authenticator,
    private val authorization: Interceptor,
    private val exponentialBackoffInterceptor: ExponentialBackoffInterceptor
) {
    fun build() : OkHttpClient {
        /**
         * Used to log info to network inspector
         *
         * You can check this [StackOverflow post](https://stackoverflow.com/questions/37105278/httplogginginterceptor-not-logging-with-retrofit-2)
         * and check [Maven Repository](https://mvnrepository.com/artifact/com.squareup.okhttp3/logging-interceptor)
         * for more information
         */
        val loggingInterceptor = HttpLoggingInterceptor()

        return OkHttpClient.Builder()
            // Content-Type header interceptor - applied first to all requests
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val request = originalRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .build()

                chain.proceed(request)
            }
            // Authorization interceptor - adds Bearer token to requests
            .addInterceptor(authorization)
            // Authenticator - handles 401 responses and refreshes tokens
            .authenticator(authenticator)
            // Network logging interceptor - logs network-level details
            .addNetworkInterceptor(loggingInterceptor)
            // Exponential backoff interceptor - retries on network errors and 5xx server errors
            // Placed last so it doesn't interfere with authentication flow
            .addInterceptor(exponentialBackoffInterceptor)
            .build()
    }
}