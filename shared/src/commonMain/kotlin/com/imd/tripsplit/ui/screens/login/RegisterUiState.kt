package com.imd.tripsplit.ui.screens.login

import kotlinx.datetime.LocalDate

data class RegisterUiState (
    val nome: String = "",
    val birthDate: LocalDate? = null,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDatePickerVisible: Boolean = false,
    val birthDateError: String? = null,
)
{
    /**
     * O botão de registro só deve estar habilitado quando
     * todos os campos estiverem preenchidos, a senha sua
     * confirmação forem iguais e não houver operação em andamento.
     */
    val isRegisterEnabled: Boolean
        get() = nome.isNotBlank() &&
                birthDate != null &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                password == confirmPassword &&
                !isLoading

    /**
     * Verifica se as senhas informadas são diferentes.
     */
    val isPasswordMatching: Boolean
        get() = confirmPassword.isEmpty() ||
                password == confirmPassword

    /**
     * Indica se a data de nascimento ainda não foi selecionada.
     */
    val isBirthDateEmpty: Boolean
        get() = birthDate == null
}