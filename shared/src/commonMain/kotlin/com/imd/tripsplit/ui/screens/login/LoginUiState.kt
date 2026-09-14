package com.imd.tripsplit.ui.screens.login

// ====================================================
// 📋 Estado da Tela de Login (State Holder Pattern)
// ====================================================

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    /**
     * O botão de login só deve estar habilitado quando ambos os campos
     * estiverem preenchidos e não houver operação em andamento.
     */
    val isLoginEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading
}
