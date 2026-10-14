@file:Suppress("FunctionName")

package com.imd.tripsplit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.imd.tripsplit.ui.screens.login.LoginScreen
import com.imd.tripsplit.ui.screens.login.RegisterScreen
import com.imd.tripsplit.ui.theme.TripSplitTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
@Preview
fun App() {
    TripSplitTheme {
        //-----
        // Anterior
        //-----
        /*LoginScreen(
            onLoginSuccess = {
                // Ponto de integração para navegar para a tela principal
                println("Login realizado com sucesso!")
            },
            onForgotPasswordClick = {
                println("Navegar para recuperação de senha")
            },
            onSignUpClick = {
                println("Navegar para tela de cadastro")
            },
        )
        */


        //-----
        // Atual
        //-----
        var currentScreen by remember {
            mutableStateOf("login")
        }

        TripSplitTheme {
            when (currentScreen) {
                "login" -> LoginScreen(
                    onRegisterClick = {
                        currentScreen = "register"
                    }
                )

                "register" -> RegisterScreen(
                    onLoginClick = {
                        println("CLIQUE EM CADASTRE-SE")
                        currentScreen = "login"
                    }
                )
            }
        }
    }
}
