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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.components.TripSplitIcons
import com.imd.tripsplit.ui.theme.BlueOcean
import com.imd.tripsplit.ui.theme.BorderLight
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.RadiusXL
import com.imd.tripsplit.ui.theme.Spacing2XL
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingXL
import com.imd.tripsplit.ui.theme.SpacingXS
import com.imd.tripsplit.ui.theme.TextDisabled
import com.imd.tripsplit.ui.theme.TextPrimary
import com.imd.tripsplit.ui.theme.TextSecondary
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.time.Instant
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


import kotlinx.datetime.toLocalDateTime

@Composable
fun RegisterContent(

    //Recebe variaveis necessárias para construção da page
    modifier: Modifier = Modifier,
    state: RegisterUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit = {},
  

){
    /* VARIAVEIS NECESSÁRIAS*/

    //Necessário para inserir a data de nascimento no formulário:
    var birthDate by remember {
        mutableStateOf<LocalDate?>(null)
    }
    val scrollState = rememberScrollState()

    /*FIM DE VARIAVEIS NECESSÁRIAS*/

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

        // ----------------------------------------------------
        // CABEÇALHO
        // ----------------------------------------------------

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

            //----> Campo Nome:

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

                leadingIcon = {
                    Icon(
                        imageVector = TripSplitIcons.User,
                        contentDescription = "Ícone de usuário",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp),
                    )
                },
                shape = RoundedCornerShape(RadiusMD),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BlueOcean,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
            )
            Spacer(modifier = Modifier.height(SpacingXS))

            //--->CAMPO DE DATA DE NASCIMENTO:

            Text(
                text = "Data de nascimento",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(SpacingXS))

            BirthDateField(
                birthDate = birthDate,
                onBirthDateChange = { birthDate = it }
            )
            Spacer(modifier = Modifier.height(SpacingXS))

            //---> CAMPO EMAIL:

            Text(
                text = "E-mail",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
            )
            Spacer(modifier = Modifier.height(SpacingXS))

            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "seu.email@exemplo.com", color = TextDisabled, fontSize = 14.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = TripSplitIcons.Email,
                        contentDescription = "Ícone de e-mail",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp),
                    )
                },
                shape = RoundedCornerShape(RadiusMD),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BlueOcean,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
            )
            Spacer(modifier = Modifier.height(SpacingXS))

            //---> CAMPO SENHA:

            Text(
                text = "Senha",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
            )
            Spacer(modifier = Modifier.height(SpacingXS))

            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "••••••••••", color = TextDisabled, fontSize = 14.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = TripSplitIcons.Lock,
                        contentDescription = "Ícone de senha",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp),
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onTogglePasswordVisibility) {
                        Icon(
                            imageVector =
                                if (state.isPasswordVisible) {
                                    TripSplitIcons.VisibilityOff
                                } else {
                                    TripSplitIcons.Visibility
                                },
                            contentDescription = if (state.isPasswordVisible) "Ocultar senha" else "Mostrar senha",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                },
                visualTransformation =
                    if (state.isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                shape = RoundedCornerShape(RadiusMD),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BlueOcean,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
            )
            Spacer(modifier = Modifier.height(SpacingXS))

            //---> CAMPO DE CONFIRMAR SENHA:

            Text(
                text = "Confirmar Senha",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
            )
            Spacer(modifier = Modifier.height(SpacingXS))

            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "••••••••••", color = TextDisabled, fontSize = 14.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = TripSplitIcons.Lock,
                        contentDescription = "Ícone de senha",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp),
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onTogglePasswordVisibility) {
                        Icon(
                            imageVector =
                                if (state.isPasswordVisible) {
                                    TripSplitIcons.VisibilityOff
                                } else {
                                    TripSplitIcons.Visibility
                                },
                            contentDescription = if (state.isPasswordVisible) "Ocultar senha" else "Mostrar senha",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                },
                visualTransformation =
                    if (state.isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                shape = RoundedCornerShape(RadiusMD),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BlueOcean,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
            )
            Spacer(modifier = Modifier.height(SpacingXS))


            //---> BOTÃO ENVIAR FORMULÁRIO:

            Button(
                onClick = onRegisterClick,
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
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = "Criar Conta!",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Spacer(modifier = Modifier.height(SpacingLG))

            //---> BOTÃO VOLTAR PARA LOGIN:

            Button(
                onClick = onLoginClick,
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
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = "Já tenho conta, quero fazer Login!",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Spacer(modifier = Modifier.height(SpacingLG))

            // ----------------------------------------------------
            //  RODAPÉ
            // ----------------------------------------------------
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDateField(
    birthDate: LocalDate?,
    onBirthDateChange: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = birthDate?.toString() ?: "",
        onValueChange = {},
        readOnly = true,
        label = { Text("Data de nascimento") },
        placeholder = { Text("DD/MM/AAAA") },
        trailingIcon = {
            IconButton(
                onClick = { showDatePicker = true }
            ) {
                Icon(
                    imageVector = TripSplitIcons.Calendar,
                    contentDescription = "Selecionar data"
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant
                                .fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.UTC)
                                .date

                            onBirthDateChange(date)
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}



