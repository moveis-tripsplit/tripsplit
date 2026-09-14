@file:Suppress("FunctionName")

package com.imd.tripsplit

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.imd.tripsplit.ui.screens.login.LoginScreen
import com.imd.tripsplit.ui.theme.TripSplitTheme

@Composable
@Preview
fun App() {
    TripSplitTheme {
        LoginScreen(
            onLoginSuccess = {
                // Ponto de integração para navegar para a tela principal
                println("Login realizado com sucesso!")
            },
            onForgotPasswordClick = {
                println("Navegar para recuperação de senha")
            },
            onSignUpClick = {
                println("Navegar para tela de cadastro")
            }
        )
    }
}
