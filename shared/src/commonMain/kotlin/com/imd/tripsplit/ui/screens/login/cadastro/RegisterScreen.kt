package com.imd.tripsplit.ui.screens.login.cadastro

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.imd.tripsplit.data.UserRepository

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegisterSuccess: () -> Unit = {},
    onLoginClick: () -> Unit = {},
) {
    var state by remember {
        mutableStateOf(RegisterUiState())
    }

    RegisterContent(
        modifier = modifier,
        state = state,
        onNameChange = {
            state = state.copy(nome = it, errorMessage = null)
        },
        onBirthDateChange = {
            state = state.copy(birthDate = it, errorMessage = null)
        },
        onEmailChange = {
            state = state.copy(email = it, errorMessage = null)
        },
        onPasswordChange = {
            state = state.copy(password = it, errorMessage = null)
        },
        onConfirmPasswordChange = {
            state = state.copy(confirmPassword = it, errorMessage = null)
        },
        onTogglePasswordVisibility = {
            state = state.copy(isPasswordVisible = !state.isPasswordVisible)
        },
        onToggleConfirmPasswordVisibility = {
            state = state.copy(isConfirmPasswordVisible = !state.isConfirmPasswordVisible)
        },
        onLoginClick = onLoginClick,
        onRegisterClick = {
            if (state.password != state.confirmPassword) {
                state = state.copy(errorMessage = "As senhas não coincidem.")
                return@RegisterContent
            }
            val result =
                UserRepository.register(
                    name = state.nome,
                    email = state.email,
                    password = state.password,
                    birthDate = state.birthDate?.toString(),
                )
            if (result.isSuccess) {
                onRegisterSuccess()
            } else {
                state = state.copy(errorMessage = result.exceptionOrNull()?.message ?: "Erro ao cadastrar usuário.")
            }
        },
    )
}
