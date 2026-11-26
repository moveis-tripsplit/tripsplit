package com.imd.tripsplit.ui.screens.login.cadastro

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.components.TripSplitIcons
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.RadiusXL
import com.imd.tripsplit.ui.theme.Spacing2XL
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingSM
import com.imd.tripsplit.ui.theme.SpacingXL
import com.imd.tripsplit.ui.theme.SpacingXS
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    state: RegisterUiState,
    onNameChange: (String) -> Unit,
    onBirthDateChange: (LocalDate?, String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    // CARD FORMULÁRIO:
    BoxWithConstraints(
        modifier =
            Modifier
                .fillMaxSize()
                .imePadding()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .navigationBarsPadding()
    ) {
        val screenWidth = maxWidth
        val isWideScreen = screenWidth >= 600.dp

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(
                        horizontal = SpacingXL,
                        vertical = Spacing2XL
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (isWideScreen) {
                // Layout Paisagem / Tablet: Row lado a lado
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = SpacingLG),
                    horizontalArrangement = Arrangement.spacedBy(Spacing2XL),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Lado Esquerdo: Texto explicativo
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Crie sua conta no TripSplit",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(SpacingMD))
                        Text(
                            text = "Preencha com seus dados,\n vem dividir despesas de viagens com seus amigos de forma simples.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Lado Direito: Formulário de Cadastro
                    Column(
                        modifier = Modifier.weight(1.2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        RegisterFormCard(
                            state = state,
                            birthDate = state.birthDate,
                            birthDateInput = state.birthDateInput,
                            onBirthDateChange = onBirthDateChange,
                            onNameChange = onNameChange,
                            onEmailChange = onEmailChange,
                            onPasswordChange = onPasswordChange,
                            onConfirmPasswordChange = onConfirmPasswordChange,
                            onTogglePasswordVisibility = onTogglePasswordVisibility,
                            onToggleConfirmPasswordVisibility = onToggleConfirmPasswordVisibility,
                            onRegisterClick = onRegisterClick,
                            onLoginClick = onLoginClick
                        )
                    }
                }
            } else {
                // Layout Retrato (Celular em pé)
                Text(
                    text = "Crie sua conta no TripSplit",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(SpacingSM))
                Text(
                    text = "Preencha com seus dados,\nvem dividir despesas de viagens com seus amigos de forma simples.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(SpacingXL))

                RegisterFormCard(
                    state = state,
                    birthDate = state.birthDate,
                    birthDateInput = state.birthDateInput,
                    onBirthDateChange = onBirthDateChange,
                    onNameChange = onNameChange,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onConfirmPasswordChange = onConfirmPasswordChange,
                    onTogglePasswordVisibility = onTogglePasswordVisibility,
                    onToggleConfirmPasswordVisibility = onToggleConfirmPasswordVisibility,
                    onRegisterClick = onRegisterClick,
                    onLoginClick = onLoginClick
                )
            }
        }
    }
}

@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    state: RegisterUiState,
    onNameChange: (String) -> Unit,
    onBirthDateChange: (LocalDate) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit = {},
) {
    RegisterContent(
        modifier = modifier,
        state = state,
        onNameChange = onNameChange,
        onBirthDateChange = { date, _ -> date?.let { onBirthDateChange(it) } },
        onEmailChange = onEmailChange,
        onPasswordChange = onPasswordChange,
        onConfirmPasswordChange = onConfirmPasswordChange,
        onTogglePasswordVisibility = onTogglePasswordVisibility,
        onToggleConfirmPasswordVisibility = onToggleConfirmPasswordVisibility,
        onLoginClick = onLoginClick,
        onRegisterClick = onRegisterClick,
    )
}

@Composable
private fun RegisterFormCard(
    state: RegisterUiState,
    birthDate: LocalDate?,
    birthDateInput: String,
    onBirthDateChange: (LocalDate?, String) -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .widthIn(max = 520.dp),
        shape = RoundedCornerShape(RadiusXL),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(SpacingXL),
        ) {
            if (state.errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = SpacingMD),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(RadiusMD),
                ) {
                    Text(
                        text = state.errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(SpacingMD),
                    )
                }
            }

            // CAMPO NOME:
            Text(
                text = "Nome",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(SpacingXS))

            OutlinedTextField(
                value = state.nome,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Nome", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = TripSplitIcons.User,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
            )
            Spacer(modifier = Modifier.height(SpacingMD))

            // CAMPO DATA DE NASCIMENTO
            Text(
                text = "Data de nascimento",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(SpacingXS))

            BirthDateField(
                birthDate = birthDate,
                birthDateInput = birthDateInput,
                onBirthDateChange = onBirthDateChange
            )
            Spacer(modifier = Modifier.height(SpacingMD))

            // CAMPO EMAIL:
            Text(
                text = "E-mail",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(SpacingXS))

            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "seu.email@exemplo.com", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = TripSplitIcons.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
            )
            Spacer(modifier = Modifier.height(SpacingMD))

            // CAMPO SENHA:
            Text(
                text = "Senha",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(SpacingXS))

            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "••••••••••", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = TripSplitIcons.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp),
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onTogglePasswordVisibility) {
                        Icon(
                            imageVector = if (state.isPasswordVisible) TripSplitIcons.VisibilityOff else TripSplitIcons.Visibility,
                            contentDescription = if (state.isPasswordVisible) "Ocultar senha" else "Mostrar senha",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(RadiusMD),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
            )
            Spacer(modifier = Modifier.height(SpacingMD))

            // CAMPO CONFIRMA SENHA:
            Text(
                text = "Confirmar Senha",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(SpacingXS))

            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "••••••••••", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = TripSplitIcons.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp),
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onToggleConfirmPasswordVisibility) {
                        Icon(
                            imageVector = if (state.isConfirmPasswordVisible) TripSplitIcons.VisibilityOff else TripSplitIcons.Visibility,
                            contentDescription = if (state.isConfirmPasswordVisible) "Ocultar senha" else "Mostrar senha",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                },
                visualTransformation = if (state.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(RadiusMD),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
            )
            Spacer(modifier = Modifier.height(SpacingXL))

            // BOTÃO CRIAR CONTA:
            Button(
                onClick = onRegisterClick,
                enabled = state.isRegisterEnabled,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                shape = RoundedCornerShape(RadiusMD),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    ),
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = "Criar Conta!",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Spacer(modifier = Modifier.height(SpacingMD))

            // BOTÃO VOLTAR PARA O LOGIN:
            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(RadiusMD),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = "Já tenho conta, quero fazer Login!",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
                Spacer(modifier = Modifier.height(SpacingLG))
            }

            //  RODAPÉ
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDateField(
    birthDate: LocalDate?,
    birthDateInput: String = "",
    onBirthDateChange: (LocalDate?, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    fun formatDateInput(input: String): String {
        val digits = input.filter { it.isDigit() }.take(8)
        val sb = StringBuilder()
        for (i in digits.indices) {
            if (i == 2 || i == 4) {
                sb.append('/')
            }
            sb.append(digits[i])
        }
        if ((digits.length == 2 || digits.length == 4) && input.endsWith('/')) {
            sb.append('/')
        }
        return sb.toString()
    }

    fun parseDate(text: String): LocalDate? {
        val digits = text.filter { it.isDigit() }
        if (digits.length != 8) return null
        val day = digits.substring(0, 2).toIntOrNull() ?: return null
        val month = digits.substring(2, 4).toIntOrNull() ?: return null
        val year = digits.substring(4, 8).toIntOrNull() ?: return null
        if (year < 1900 || year > 2100) return null
        if (month !in 1..12) return null
        if (day !in 1..31) return null
        return try {
            LocalDate(year, month, day)
        } catch (_: Exception) {
            null
        }
    }

    val displayValue =
        if (birthDateInput.isNotEmpty()) {
            birthDateInput
        } else {
            birthDate?.let {
                val day = it.dayOfMonth.toString().padStart(2, '0')
                val month = it.monthNumber.toString().padStart(2, '0')
                val year = it.year.toString().padStart(4, '0')
                "$day/$month/$year"
            } ?: ""
        }

    val digitsCount = displayValue.filter { it.isDigit() }.length
    val isDateInvalid = digitsCount == 8 && birthDate == null

    OutlinedTextField(
        value = displayValue,
        onValueChange = { newValue ->
            val formatted = formatDateInput(newValue)
            val parsedDate = parseDate(formatted)
            onBirthDateChange(parsedDate, formatted)
        },
        modifier = modifier.fillMaxWidth(),
        readOnly = false,
        isError = isDateInvalid,
        supportingText =
            if (isDateInvalid) {
                {
                    Text(
                        text = "Data inválida. Use o formato DD/MM/AAAA",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                    )
                }
            } else {
                null
            },
        placeholder = {
            Text(
                text = "DD/MM/AAAA",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
            )
        },
        leadingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = TripSplitIcons.Calendar,
                    contentDescription = "Selecionar data de nascimento",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
            }
        },
        shape = RoundedCornerShape(RadiusMD),
        singleLine = true,
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
    )

    if (showDatePicker) {
        val initialMillis =
            birthDate?.atStartOfDayIn(TimeZone.UTC)?.toEpochMilliseconds()
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = initialMillis,
            )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date =
                                Instant
                                    .fromEpochMilliseconds(millis)
                                    .toLocalDateTime(TimeZone.UTC)
                                    .date
                            val day = date.dayOfMonth.toString().padStart(2, '0')
                            val month = date.monthNumber.toString().padStart(2, '0')
                            val year = date.year.toString().padStart(4, '0')
                            val formatted = "$day/$month/$year"
                            onBirthDateChange(date, formatted)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun BirthDateField(
    birthDate: LocalDate?,
    onBirthDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    BirthDateField(
        birthDate = birthDate,
        birthDateInput = "",
        onBirthDateChange = { date, _ -> date?.let { onBirthDateChange(it) } },
        modifier = modifier,
    )
}
