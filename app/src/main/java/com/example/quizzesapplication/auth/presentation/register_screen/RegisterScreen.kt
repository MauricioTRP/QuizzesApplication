package com.example.quizzesapplication.auth.presentation.register_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.quizzesapplication.auth.domain.PASSWORD_MIN_LENGTH
import com.example.quizzesapplication.auth.domain.PasswordValidationState
import com.example.quizzesapplication.common.presentation.EyeClosed
import com.example.quizzesapplication.common.presentation.EyeOpen
import com.example.quizzesapplication.ui.theme.QuizzesApplicationTheme

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onLoginClick: () -> Unit, // used for navigation
    onLoggedIn: () -> Unit,
    showSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    /**
     *
     */

    RegisterScreenStateless(
        currentUIState = viewModel.uiState,
        onPasswordUpdate = { newValue ->
            viewModel.updatePasswordTextFieldValue(newValue)
        },
        onEmailUpdate = { newValue ->
            viewModel.updateEmailTextFieldValue(newValue)
        },
        onLoginClick = onLoginClick,
        onTogglePasswordVisibilityClick = {
            viewModel.togglePasswordVisibility()
        },
        onRegisterClick = {
            viewModel.register()
        },
        onTOSClick = { isAccepted ->
            viewModel.toggleTOS(isAccepted)
        },
        modifier = modifier
    )

    /**
     * Snackbar message in case of Register Error
     */
    if (viewModel.uiState.hasRegisterError) {
        showSnackbar(stringResource(R.string.registration_error))
    }

    /**
     * Navigate in case of successful registration
     */
    if(viewModel.uiState.isLoggedIn) {
        showSnackbar(stringResource(R.string.registration_successful))
        onLoggedIn()
    }
}

@Composable
fun RegisterScreenStateless(
    currentUIState: RegisterUIState, // pass a copy of current state
    onEmailUpdate: (TextFieldValue) -> Unit, // lambda to handle email update on TextField
    onPasswordUpdate: (TextFieldValue) -> Unit, // lambda to handle password update on TextField
    onTogglePasswordVisibilityClick: () -> Unit, // Toggle Password Visibility
    onRegisterClick: () -> Unit, // used to handle register request
    onLoginClick: () -> Unit, // lambda to navigate to login screen
    onTOSClick: (Boolean) -> Unit, // Handle accepted terms and conditions
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
            text = stringResource(R.string.welcome_message) + "\n" + stringResource(R.string.please_login),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m)))
        // Annotated string to change to register form
        Text(
            buildAnnotatedString {
                // TODO: Check how to use Material Theme default text stype as "SpanStyle"
                append(stringResource(R.string.already_have_an_account) + " ")

                val link = LinkAnnotation.Clickable(
                    tag = stringResource(R.string.login),
                    linkInteractionListener = {
                        onLoginClick()
                    },
                    styles = TextLinkStyles(SpanStyle(color = MaterialTheme.colorScheme.primary))
                )
                withLink(link) {
                    append(stringResource(R.string.login))
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
            supportingText = {
                Text(stringResource(R.string.must_be_valid_email))
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

        // TOS

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(stringResource(R.string.accept_tos))
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_s)))
            Checkbox(
                checked = currentUIState.acceptedTOS,
                onCheckedChange = { onTOSClick(it) }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))

        // Register Button
        Button(
            onClick = onRegisterClick,
            enabled = currentUIState.canRegister,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.register)
            )
        }

        // Validations UI for User
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xxl)))

        // Min Length Password Requirement
        PasswordRequirement(
            isValid = currentUIState.passwordValidationState.hasMinLength,
            text = stringResource(R.string.has_min_length)
                    + " ${PASSWORD_MIN_LENGTH} "
                    + stringResource(R.string.characters)
        )

        // Number password requirement
        PasswordRequirement(
            isValid = currentUIState.passwordValidationState.hasNumber,
            text = stringResource(R.string.password_must_have_a_number)
        )

        // LowerCase password requirement
        PasswordRequirement(
            isValid = currentUIState.passwordValidationState.hasLowerCaseCharacter,
            text = stringResource(R.string.password_must_have_a_lowercase)
        )

        // Uppercase password requirement
        PasswordRequirement(
            isValid = currentUIState.passwordValidationState.hasUpperCaseCharacter,
            text = stringResource(R.string.password_must_have_an_uppercase)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    var passwordState by remember {
        mutableStateOf(
            PasswordValidationState(
                hasMinLength = false,
                hasNumber = true,
                hasLowerCaseCharacter = true,
                hasUpperCaseCharacter = false
            )
        )
    }

    var state by remember {
        mutableStateOf(RegisterUIState(
            email = TextFieldValue(),
            password = TextFieldValue(),
            isPasswordVisible = true,
            canRegister = true,
            isRegistering = false,
            passwordValidationState = passwordState
        ))
    }

    QuizzesApplicationTheme() {
        RegisterScreenStateless(
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
            onTOSClick = {}
        )
    }
}

@Composable
fun PasswordRequirement(
    text: String,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Icon(
            imageVector = if (isValid) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (!isValid) MaterialTheme.colorScheme.error else Color.Green
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_m)))
        // Text
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium
        )

    }
}
