package com.example.quizzesapplication.common.di

import com.example.quizzesapplication.common.data.network.ApiConfig
import com.example.quizzesapplication.common.data.network.AuthenticatorInterceptor
import com.example.quizzesapplication.common.data.network.AuthorizationInterceptor
import com.example.quizzesapplication.common.data.network.ExponentialBackoffInterceptor
import com.example.quizzesapplication.common.data.network.HttpClientFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Interceptor
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class HttpClientModule {
    /**
     * AuthenticatorInterceptor
     */
    @Binds
    abstract fun bindsAuthenticator(authenticatorInterceptor: AuthenticatorInterceptor) : Authenticator

    /**
     * Authorization Interceptor
     */
    @Binds
    abstract fun bindsAuthorization(authorizationInterceptor: AuthorizationInterceptor) : Interceptor
    companion object {
        /**
         * Retrofit Instance
         */
        @Provides
        @Singleton
        fun providesRetrofitInstance(
            okHttpClient: OkHttpClient
        ) : Retrofit {
            val json = Json {
                ignoreUnknownKeys = true
                isLenient = true
            }

            return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .baseUrl(ApiConfig.BASE_URL)
                .build()
        }

        /**
         * OkHttp Client
         */
        @Provides
        @Singleton
        fun provideOkHttpClient(httpClientFactory: HttpClientFactory) : OkHttpClient =
            httpClientFactory.build()

        /**
         * Provide [ExponentialBackoffInterceptor]
         */
        @Provides
        @Singleton
        fun provideExponentialBackoffInterceptor() : ExponentialBackoffInterceptor =
            ExponentialBackoffInterceptor()

    }
}