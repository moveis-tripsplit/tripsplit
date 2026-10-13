package com.imd.tripsplit.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.S
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.components.TripSplitIcons
import com.imd.tripsplit.ui.theme.BorderLight
import com.imd.tripsplit.ui.theme.RadiusXL
import com.imd.tripsplit.ui.theme.Spacing2XL
import com.imd.tripsplit.ui.theme.SpacingXL
import com.imd.tripsplit.ui.theme.SpacingXS
import com.imd.tripsplit.ui.theme.TextDisabled
import com.imd.tripsplit.ui.theme.TextPrimary
import com.imd.tripsplit.ui.theme.TextSecondary

@Composable
fun RegisterContent(

    //Recebe variaveis necessárias para construção da page
    modifier: Modifier = Modifier,
    state: RegisterUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,

){
    val scrollState = rememberScrollState()

    /* Coluna Principal */
    Column (
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .imePadding()
                .padding(horizontal = SpacingXL, vertical = Spacing2XL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        /* CABEÇALHO */

        //to do

        //Texto
        Text(
            text = "Olá! Vamos realizar seu cadastro preenchendo os campos abaixo.",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center,
        )



        /* CARD FORMULÁRIO */
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(RadiusXL),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, BorderLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            /*COLUMN DO CARD PARA COLOCAR OS CAMPOS PARA PREENCHER*/

            //----> Campo Nome
            Text(
                text = "Nome",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(SpacingXS))
            OutlinedTextField(
                value = state.nome,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Nome", color = TextDisabled, fontSize = 14.sp)
                },

                /*
                leadingIcon = {
                    Icon(
                        imageVector = TripSplitIcons.Email,
                        contentDescription = "Ícone de e-mail",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp),
                    )
                 */
            )


            //Config caixinha para preencher

            Spacer(modifier = Modifier.height(SpacingXS))
            //Caixinha data de nascimento

            Spacer(modifier = Modifier.height(SpacingXS))

            Spacer(modifier = Modifier.height(SpacingXS))
            //caixinha email

            Spacer(modifier = Modifier.height(SpacingXS))

            Spacer(modifier = Modifier.height(SpacingXS))
            //Caixinha senha

            Spacer(modifier = Modifier.height(SpacingXS))

            Spacer(modifier = Modifier.height(SpacingXS))
            //Caixinha confirmar senha

            Spacer(modifier = Modifier.height(SpacingXS))
            Spacer(modifier = Modifier.height(SpacingXS))


        }

        //Botão confirmar formulario para registro
        //Botão de retornar caso já tenha conta para fazer o login
    }


}


