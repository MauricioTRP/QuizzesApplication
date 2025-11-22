package com.example.quizzesapplication.auth.presentation.login_screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.quizzesapplication.R
import com.example.quizzesapplication.common.presentation.EyeClosed
import com.example.quizzesapplication.common.presentation.EyeOpen
import com.example.quizzesapplication.ui.theme.QuizzesApplicationTheme

@Composable
fun LoginScreenStateless(
    currentUIState: LoginUIState, // pass a copy of current state
    onEmailUpdate: (TextFieldValue) -> Unit, // lambda to handle email update on TextField
    onPasswordUpdate: (TextFieldValue) -> Unit, // lambda to handle password update on TextField
    onTogglePasswordVisibilityClick: () -> Unit, // Toggle Password Visibility
    onRegisterClick: () -> Unit, // used to navigate to register screen
    onLoginClick: () -> Unit, // lambda to handle Login Request
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.padding_small))
            .padding(top = dimensionResource(R.dimen.padding_large))

    ) {
        Text(
            text = stringResource(R.string.hi),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = stringResource(R.string.welcome_message),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m)))
        // Annotated string to change to register form
        Text(
            buildAnnotatedString {
                // TODO: Check how to use Material Theme default text type as "SpanStyle"
                append(stringResource(R.string.dont_have_account) + " ")

                val link = LinkAnnotation.Clickable(
                    tag = stringResource(R.string.register),
                    linkInteractionListener = {
                        onRegisterClick()
                    },
                    styles = TextLinkStyles(SpanStyle(color = MaterialTheme.colorScheme.primary))
                )
                withLink(link) {
                    append(stringResource(R.string.register))
                }
            }
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))

        // Email
        TextField(
            value = currentUIState.email,
            onValueChange = { onEmailUpdate(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = { Text(stringResource(R.string.email_label)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_large)))
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m)))

        // Password
        TextField(
            value = currentUIState.password,
            onValueChange = { onPasswordUpdate(it) },
            visualTransformation = if (!currentUIState.isPasswordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = { Text(stringResource(R.string.password_label)) },
            trailingIcon = {
                Icon(
                    imageVector = if (currentUIState.isPasswordVisible) {
                        EyeOpen
                    } else EyeClosed,
                    contentDescription = if (currentUIState.isPasswordVisible) {
                        stringResource(R.string.show_password)
                    } else stringResource(R.string.hide_password),
                    modifier = Modifier
                        .clickable(
                            enabled = true,
                            onClick = onTogglePasswordVisibilityClick
                        )
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_large)))
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))
        Button(
            onClick = onLoginClick,
            enabled = currentUIState.canLogin,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.login)
            )
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onRegisterClick: () -> Unit,
    onLoggedIn: () -> Unit,
    showSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LoginScreenStateless(
        currentUIState = viewModel.uiState,
        onPasswordUpdate = { newValue ->
            viewModel.updatePasswordTextFieldValue(newValue)
        },
        onEmailUpdate = { newValue ->
            viewModel.updateEmailTextFieldValue(newValue)
        },
        onLoginClick = {
            Log.d("LoginScreen", "Login Clicked")
            viewModel.login()
        },
        onTogglePasswordVisibilityClick = {
            viewModel.togglePasswordVisibility()
        },
        onRegisterClick = onRegisterClick,
        modifier = modifier
    )

    /**
     * Snackbar message in case of Login Error
     */
    if (viewModel.uiState.hasLoginError) {
        showSnackbar(stringResource(R.string.login_error))
    }

    /**
     * Navigate in case of successful login
     */
    if(viewModel.uiState.isLoggedIn) {
        showSnackbar(stringResource(R.string.login_successful))
        onLoggedIn()
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    var state by remember {
        mutableStateOf(LoginUIState(
            email = TextFieldValue(),
            password = TextFieldValue(),
            isPasswordVisible = true,
            canLogin = true,
            isLoggingIn = false
        ))
    }

    QuizzesApplicationTheme() {
        LoginScreenStateless(
            currentUIState = state,
            onEmailUpdate = { newEmail ->
                state = state.copy(email = newEmail)
            },
            onPasswordUpdate = { newPassword ->
                state = state.copy(password = newPassword)
            },
            onLoginClick = {},
            onTogglePasswordVisibilityClick = {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            },
            onRegisterClick = {},
        )
    }
}