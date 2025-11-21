package com.example.quizzesapplication.common.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.quizzesapplication.common.data.local.TokenProviderImpl
import com.example.quizzesapplication.common.domain.AuthTokensProtoSerializer
import com.example.quizzesapplication.common.domain.TokenProvider
import com.example.quizzesapplication.proto.AuthTokensProto
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataDependencies {
    /**
     * Binding between `TokenProvider` and `TokenProviderImpl`
     */
    @Binds
    @Singleton
    abstract fun bindTokenProvider(impl: TokenProviderImpl) : TokenProvider

    /**
     * Binding between `OnboardingChecker` and `OnboardingCheckerImpl`
     */
//    @Binds
//    @Singleton
//    abstract fun bindOnboardingChecker(impl: OnboardingCheckerImpl) : OnboardingChecker

    companion object {
        /**
         * proto stores reading from .proto files
         */
        private val Context.sessionTokenStore: DataStore<AuthTokensProto> by dataStore<AuthTokensProto>(
            fileName = "session_token.pb",
            serializer = AuthTokensProtoSerializer
        )


        /**
         * Onboarding flag token from .proto file
         */
//        private val Context.onboardingFlag: DataStore<OnboardingFlagProto> by dataStore<OnboardingFlagProto>(
//            fileName = "onboarding_flag.pb",
//            serializer = OnboardingFlagSerializer
//        )

        /**
         * Dependency provider for Session Token DataStore
         */
        @Provides
        @Singleton
        fun provideSessionTokenStore(
            @ApplicationContext context: Context
        ): DataStore<AuthTokensProto> {
            return context.sessionTokenStore
        }

        /**
         * Dependency provider for Onboarding DataStore
         */
//        @Provides
//        @Singleton
//        fun provideOnboardingFlagStore(
//            @ApplicationContext context: Context
//        ): DataStore<OnboardingFlagProto> = context.onboardingFlag
    }
}