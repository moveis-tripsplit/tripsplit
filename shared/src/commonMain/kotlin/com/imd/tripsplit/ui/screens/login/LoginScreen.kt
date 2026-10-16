package com.imd.tripsplit.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.components.TripSplitIcons
import com.imd.tripsplit.ui.components.TripSplitLogo
import com.imd.tripsplit.ui.theme.BlueOcean
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.RadiusXL
import com.imd.tripsplit.ui.theme.Spacing2XL
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingSM
import com.imd.tripsplit.ui.theme.SpacingXL
import com.imd.tripsplit.ui.theme.SpacingXS
import com.imd.tripsplit.ui.theme.TextSecondary
import com.imd.tripsplit.ui.theme.TripSplitTheme


// ====================================================
// 📱 Tela de Login - TripSplit
// ====================================================

/**
 * Versão Stateful (com estado):
 * Gerencia o estado localmente para facilitar uso direto e testes.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    var state by remember { mutableStateOf(LoginUiState()) }

    LoginContent(
        state = state,
        onEmailChange = { state = state.copy(email = it) },
        onPasswordChange = { state = state.copy(password = it) },
        onTogglePasswordVisibility = { state = state.copy(isPasswordVisible = !state.isPasswordVisible) },
        onRememberMeChange = { state = state.copy(rememberMe = it) },
        onLoginClick = {
            // Simulação de ação de login
            onLoginSuccess()
        },
        onForgotPasswordClick = onForgotPasswordClick,
        onRegisterClick = onRegisterClick,
        modifier = modifier,
    )
}

/**
 * Versão Stateless (sem estado - pura):
 * Recebe o estado e apenas dispara callbacks de eventos (State Hoisting).
 */
@Composable
fun LoginContent(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
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
        // 1. Cabeçalho: Logo + Marca + Boas-vindas
        // ----------------------------------------------------
        Spacer(modifier = Modifier.height(SpacingLG))

        TripSplitLogo(size = 76.dp)

        Spacer(modifier = Modifier.height(SpacingSM))

        // Título estilizado: "Trip" (escuro) + "Split" (azul)
        val appTitle =
            buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)) {
                    append("Trip")
                }
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                    append("Split")
                }
            }
        Text(
            text = appTitle,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(SpacingSM))

        // Mensagem de boas-vindas
        Text(
            text = "Bem-vindo de volta!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(SpacingXS))

        // Subtítulo descritivo
        Text(
            text = "Acesse sua conta para gerenciar despesas\ne viagens em grupo sem complicações.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
        )

        Spacer(modifier = Modifier.height(SpacingXL))

        // ----------------------------------------------------
        // 2. Card de Formulário
        // ----------------------------------------------------
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(RadiusXL),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(
                modifier = Modifier.padding(SpacingXL),
            ) {
                // Campo: E-mail
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
                        Text(
                            text = "seu.email@exemplo.com",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = TripSplitIcons.Email,
                            contentDescription = "Ícone de e-mail",
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
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = MaterialTheme.colorScheme.primary,
                        ),
                )

                Spacer(modifier = Modifier.height(SpacingLG))

                // Campo: Senha
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
                        Text(
                            text = "••••••••••",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = TripSplitIcons.Lock,
                            contentDescription = "Ícone de senha",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    keyboardActions =
                        KeyboardActions(
                            onDone = { if (state.isLoginEnabled) onLoginClick() },
                        ),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = MaterialTheme.colorScheme.primary,
                        ),
                )

                Spacer(modifier = Modifier.height(SpacingMD))

                // Linha: Lembrar de mim + Esqueceu a senha
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Checkbox "Lembrar de mim"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onRememberMeChange(!state.rememberMe) },
                    ) {
                        Checkbox(
                            checked = state.rememberMe,
                            onCheckedChange = onRememberMeChange,
                            colors =
                                CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                    uncheckedColor = MaterialTheme.colorScheme.outline,
                                ),
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.size(SpacingSM))
                        Text(
                            text = "Lembrar de mim",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    // Link "Esqueceu a senha?"
                    Text(
                        text = "Esqueceu a senha?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(onClick = onForgotPasswordClick),
                    )
                }

                Spacer(modifier = Modifier.height(SpacingXL))

                // Botão Primário: Entrar na conta
                Button(
                    onClick = onLoginClick,
                    enabled = state.isLoginEnabled,
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
                            text = "Entrar na conta",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(SpacingLG))

                // Link para cadastro: Não tem uma conta? Cadastre-se
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Não tem uma conta? ",
                        fontSize = 14.sp,
                        color = TextSecondary,
                    )

                    Text(
                        text = "Cadastre-se",
                        modifier =
                            Modifier.clickable {
                                onRegisterClick()
                            },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlueOcean,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing2XL))

        // ----------------------------------------------------
        // 3. Rodapé de Confiança e Criptografia
        // ----------------------------------------------------
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = TripSplitIcons.ShieldCheck,
                contentDescription = "Proteção",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = "Dados protegidos com criptografia",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(modifier = Modifier.height(SpacingLG))
    }
}

// ====================================================
// 🔍 Preview para Android Studio e Desktop
// ====================================================
@Preview
@Composable
fun LoginScreenLightPreview() {
    TripSplitTheme(darkTheme = false) {
        LoginScreen()
    }
}

@Preview
@Composable
fun LoginScreenDarkPreview() {
    TripSplitTheme(darkTheme = true) {
        LoginScreen()
    }
}
