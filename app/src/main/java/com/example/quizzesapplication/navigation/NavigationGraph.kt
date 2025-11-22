package com.example.quizzesapplication.navigation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.quizzesapplication.auth.presentation.login_screen.LoginScreen
import com.example.quizzesapplication.auth.presentation.register_screen.RegisterScreen
import com.example.quizzesapplication.quizzes.presentation.QuizLandingScreen
import com.example.quizzesapplication.quizzes.presentation.QuizzesViewModel
import com.example.quizzesapplication.quizzes.presentation.components.QuestionWithOptionComposable


@Composable
fun NavigationComposable(
    navController: NavHostController, // NavController is created in MainActivity
    isLoggedIn: Boolean, // Check if the user have started a session
    showSnackbar: (String) -> Unit, // Lambda to show a snackbar message
    haveDoneOnboarding: Boolean, // Check if user have already done onboarding
    // mainViewModel: MainViewModel, // ViewModel to be used by onboarding graph
    modifier: Modifier = Modifier // Modifier to be used inside Scaffold of MainActivity
) {
    NavHost(
        navController = navController,
        startDestination = checkInitialRoute(isLoggedIn = isLoggedIn, haveDoneOnboarding = haveDoneOnboarding)
    ) {
        quizzesGraph(
            navController = navController,
            showSnackbar = showSnackbar,
            modifier = modifier
        )
        authGraph(
            navController = navController,
            showSnackbar = showSnackbar,
            modifier = modifier
        )
    }
}

/**
 * `NavGraphBuilder.authGraph` works like a navController only to `auth` module
 *
 * @param navController - is passed the same navController that MainActivity uses
 * @param showSnackbar - is passed the same showSnackbar that MainActivity uses
 */
private fun NavGraphBuilder.authGraph(
    navController: NavController,
    showSnackbar: (String) -> Unit,
    modifier: Modifier
) {
    navigation(
        startDestination = AuthScreens.Intro.route,
        route = AuthScreens.Root.route
    ) {
        /**
         * AuthScreen Intro Composable
         */
        composable(route = AuthScreens.Intro.route) {
            Column(
                modifier = modifier
            ) {
                Text("Intro Route of Auth Package/Module")
                Button(
                    onClick = {
                        navController.navigate(AuthScreens.Login.route)
                    }
                ) {
                    Text("Go to Login")
                }
            }
        }

        /**
         * AuthScreen Login Composable
         */
        composable(route = AuthScreens.Login.route) {
            LoginScreen(
                viewModel = androidx.hilt.navigation.compose.hiltViewModel(),
                onRegisterClick = { // Navigates to Register Composable
                    navController.navigate(AuthScreens.Register.route) {
                        popUpTo(AuthScreens.Login.route) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onLoggedIn = {
                    Log.d("NavGraph", "Successfully logged in")
                    navController.navigate(QuizzesScreens.QuizMainScreen.route)
                },
                showSnackbar = showSnackbar,
                modifier = modifier,
            )
        }

        /**
         * Register Composable
         */
        composable(route = AuthScreens.Register.route) {

            RegisterScreen(
                viewModel = androidx.hilt.navigation.compose.hiltViewModel(),
                onLoginClick = {
                    navController.navigate(AuthScreens.Login.route) {
                        popUpTo(AuthScreens.Register.route) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onLoggedIn = {
                    navController.navigate(QuizzesScreens.QuizMainScreen.route)
                },
                showSnackbar = showSnackbar,
                modifier = modifier
            )
        }

    }
}

private fun NavGraphBuilder.quizzesGraph(
    navController: NavController,
    showSnackbar: (String) -> Unit,
    modifier: Modifier
) {
    navigation(startDestination = QuizzesScreens.QuizMainScreen.route, route = QuizzesScreens.Root.route) {
        composable(route = QuizzesScreens.QuizMainScreen.route) {
            // Way to handle shared ViewModels between views
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QuizzesScreens.Root.route)
            }

            /**
             * ```kotlin
             * val parentEntry = remember(it) {
             *     navController.getBackStackEntry(QuizzesScreens.Root.route)
             * }
             *
             * val quizzesViewModel = hiltViewModel<QuizzesViewModel>(parentEntry)
             * ```
             *
             * Creates a single instance of [QuizzesViewModel] and share it across all composables
             * of the [quizzesGraph] (Staring from [QuizzesScreens.Root.route])
             */
            val quizzesViewModel = hiltViewModel<QuizzesViewModel>(parentEntry)
            QuizLandingScreen(
                onStartQuiz = {
                    navController
                        .navigate(QuizzesScreens.QuizDetailScreen.createRoute(it.toIntOrNull() ?: 0))
                },
                showSnackbar = showSnackbar,
                quizzesViewModel = quizzesViewModel,
                modifier = modifier
            )
        }

        // Detail Screen (Quiz by Id)
        composable(
            route = QuizzesScreens.QuizDetailScreen.route,
            arguments = listOf(navArgument("quizId") { type = NavType.StringType })
        ) { backStackEntry ->
            val quizId = backStackEntry.arguments?.getString("quizId")?.toIntOrNull() ?: 0
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(QuizzesScreens.Root.route)
            }
            val quizzesViewModel = hiltViewModel<QuizzesViewModel>(parentEntry)

            QuestionWithOptionComposable(
                viewModel = quizzesViewModel,
                quizId = quizId,
                onSubmitAnswer = { solvedQuizId ->
                    val nextQuizId = quizzesViewModel.updateCurrentQuiz(solvedQuizId)
                    val route = if (nextQuizId == "end") {
                        QuizzesScreens.QuizMainScreen.route
                    } else {
                        QuizzesScreens.QuizDetailScreen.createRoute(nextQuizId.toIntOrNull() ?: 0)
                    }
                    navController.navigate(route = route) {
                        popUpTo(QuizzesScreens.QuizDetailScreen.createRoute(solvedQuizId)) {
                            inclusive = true
                        }
                    }
                },
                modifier = modifier,
            )
        }
    }
}

/**
 * Check where the user must start in the navigation route.
 * Analyses the logged in status as well as the onboarding status
 *
 * @return [String] instance, representing the initial route
 */
private fun checkInitialRoute(
    isLoggedIn: Boolean,
    haveDoneOnboarding: Boolean
) : String {
    return if (!isLoggedIn) {
        AuthScreens.Root.route
    } else if (!haveDoneOnboarding){
        OnboardingScreens.Root.route
    } else {
        QuizzesScreens.Root.route
    }
}
