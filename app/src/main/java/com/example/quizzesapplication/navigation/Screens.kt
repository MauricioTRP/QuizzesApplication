package com.example.quizzesapplication.navigation

import androidx.compose.ui.graphics.vector.ImageVector


/**
 * Using sealed interfaces for Screens of packages or modules
 * reduces errors due mistyping
 */
interface Screen {
    val icon: ImageVector?
        get() = null
    val route: String
}

/**
 * Auth Screens
 */
sealed interface AuthScreens{
    /**
     * Root Screens works like an entry point to the Graph of the `auth` package
     */
    data object Root : AuthScreens, Screen {
        override val route: String
            get() = "auth"
    }

    data object Intro : AuthScreens, Screen {
        override val route: String
            get() = "intro"
    }

    data object Login : AuthScreens, Screen {
        override val route: String
            get() = "login"
    }

    data object Register: AuthScreens, Screen {
        override val route: String
            get() = "register"
    }
}

/**
 * Quizzes Screens
 */
sealed interface QuizzesScreens{
    data object Root : QuizzesScreens, Screen {
        override val route: String
            get() = "quizzes_root"
    }

    data object QuizMainScreen : QuizzesScreens, Screen {
        override val route: String
            get() = "quiz_list"
    }

    data object QuizDetailScreen : QuizzesScreens, Screen {
        private const val ROUTE_PREFIX = "quiz_detail"
        override val route: String
            get() = "$ROUTE_PREFIX/{quizId}"

        fun createRoute(quizId: String) = "$ROUTE_PREFIX/$quizId"
    }
}

/**
 * Onboarding Screens
 */
sealed interface OnboardingScreens {
    data object Root : OnboardingScreens, Screen {
        override val route: String
            get() = "onboarding_root"
    }

    data object OnboardingJourneyScreen : OnboardingScreens, Screen {
        override val route: String
            get() = "onboarding_journey"
    }
}
