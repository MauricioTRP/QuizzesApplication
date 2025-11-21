package com.example.quizzesapplication.common.data.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.math.pow

/**
 * Intercepts failed network requests and retries them with an exponential backoff delay.
 *
 * @param maxRetries The maximum number of retries.
 * @param initialDelayMillis The initial delay in milliseconds.
 */
class ExponentialBackoffInterceptor(
    private val maxRetries: Int = 3,
    private val initialDelayMillis: Long = 1000L
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var exception: IOException? = null
        var tryCount = 0

        while (tryCount < maxRetries) {
            tryCount++

            try {
                response?.close() // Close the previous response before retrying, avoid resource leak
                response = chain.proceed(request)

                if (response.isSuccessful) {
                    return response
                }
            } catch (e: IOException) {
                exception = e

                if (tryCount >= maxRetries) {
                    throw e
                }
            }

            if(tryCount < maxRetries) {
                val delayMillis = initialDelayMillis * 2.0.pow(tryCount.toDouble() - 1.0).toLong()
                runBlocking {
                    delay(delayMillis)
                }

            }
        }

        if (response == null) {
            throw exception ?: IOException("Failed after $maxRetries retries")
        }

        return response
    }
}