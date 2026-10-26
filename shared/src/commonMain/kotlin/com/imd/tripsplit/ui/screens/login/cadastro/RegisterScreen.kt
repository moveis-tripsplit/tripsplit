package com.imd.tripsplit.ui.screens.login.cadastro

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
) {
    var state by remember {
        mutableStateOf(RegisterUiState())
    }

    RegisterContent(
        modifier = modifier,
        state = state,
        onNameChange = {
            state = state.copy(nome = it)
        },
        onEmailChange = {
            state = state.copy(email = it)
        },
        onPasswordChange = {
            state = state.copy(password = it)
        },
        onTogglePasswordVisibility = {
            state =
                state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
        },
        onLoginClick = onLoginClick,
        onRegisterClick = onRegisterClick
    )
}
