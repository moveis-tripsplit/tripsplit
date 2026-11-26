package com.imd.tripsplit.ui.screens.login.cadastro

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import com.imd.tripsplit.presentation.register.RegisterViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
) {
    val viewModel: RegisterViewModel = viewModel { RegisterViewModel() }
    val state by viewModel.state.collectAsState()

    RegisterContent(
        modifier = modifier,
        state = state,
        onNameChange = viewModel::onNameChange,
        onBirthDateChange = viewModel::onBirthDateChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
        onLoginClick = onLoginClick,
        onRegisterClick = onRegisterClick
    )
}
