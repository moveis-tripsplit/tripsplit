package com.imd.tripsplit.ui.screens.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.imd.tripsplit.ui.theme.BlueOcean
import com.imd.tripsplit.ui.theme.RadiusMD

@Composable
fun ForgetPasswordContent(
    // Recebe variaveis necessárias para construção da page
    modifier: Modifier = Modifier,
    state: ForgetPasswordUIState,
    onEmailChange: (String) -> Unit,
    onForgetClick: () -> Unit,
    onLoginClick: () -> Unit = {},
) {

    //Construcao da pagina: Layout

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        //Primario
        Text("Encontre sua conta!")
        //Secundario
        Text("Insira seu email")
        // Campo de e-mail

        //Botão para receber código para recuperação
        Button(
            onClick = onForgetClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            shape = RoundedCornerShape(RadiusMD),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = BlueOcean,
                    contentColor = Color.White,
                    disabledContainerColor = BlueOcean,
                    disabledContentColor = Color.White,
                ),
        ) {



        }

    }

}