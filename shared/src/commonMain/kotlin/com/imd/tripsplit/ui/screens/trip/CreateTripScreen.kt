package com.imd.tripsplit.ui.screens.trip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.theme.BlueOcean
import com.imd.tripsplit.ui.theme.BorderLight
import com.imd.tripsplit.ui.theme.GreenTravel
import com.imd.tripsplit.ui.theme.OrangeAccent
import com.imd.tripsplit.ui.theme.RadiusLG
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.RadiusSM
import com.imd.tripsplit.ui.theme.RadiusXL
import com.imd.tripsplit.ui.theme.Spacing2XL
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingSM
import com.imd.tripsplit.ui.theme.SpacingXL
import com.imd.tripsplit.ui.theme.SpacingXS
import com.imd.tripsplit.ui.theme.TextDisabled
import com.imd.tripsplit.ui.theme.TextPrimary
import com.imd.tripsplit.ui.theme.TextSecondary

// ====================================================
// ✈️ Tela de Cadastro de Viagem - TripSplit
// ====================================================

/**
 * Versão Stateful (com estado):
 * Gerencia o estado localmente e expõe callbacks de navegação.
 */
@Composable
fun CreateTripScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onTripCreatedSuccess: (tripId: String) -> Unit = {},
) {
    // Inicialização com dados padrão de exemplo
    var state by remember {
        mutableStateOf(
            CreateTripUiState(
                title = "Férias em Florianópolis 🏖️",
                description = "Viagem com os amigos para curtir praias do norte, passeios e churrasco na casa alugada.",
                startDate = "15/11/2026",
                endDate = "22/11/2026",
                activities = listOf(
                    TripActivity("1", "Check-in na Casa", "15/11 • 14:00", "Praia de Canasvieiras"),
                    TripActivity("2", "Passeio de Barco", "16/11 • 10:00", "Ilha do Campeche", 120.0),
                ),
                participants = listOf(
                    TripParticipant("1", "Você", "VC", "Organizador", isOrganizer = true),
                    TripParticipant("2", "Beatriz Camilo", "BC", "Confirmada"),
                    TripParticipant("3", "Clara Macedo", "CM", "Membro"),
                    TripParticipant("4", "Fernando Santos", "FS", "Sem conta", hasAccount = false),
                ),
            )
        )
    }

    CreateTripContent(
        state = state,
        onTitleChange = { state = state.copy(title = it) },
        onDescriptionChange = { state = state.copy(description = it) },
        onStartDateChange = { state = state.copy(startDate = it) },
        onEndDateChange = { state = state.copy(endDate = it) },
        onAddActivityClick = {
            // Em fluxo completo abriria o modal; aqui adiciona uma nova atividade de demonstração
            val newAct = TripActivity(
                id = (state.activities.size + 1).toString(),
                title = "Trilha da Lagoinha",
                dateHour = "17/11 • 09:00",
                location = "Praia do Matadeiro",
            )
            state = state.copy(activities = state.activities + newAct)
        },
        onAddParticipantClick = {
            // Demonstração de inclusão de participante dinâmico
            val newP = TripParticipant(
                id = (state.participants.size + 1).toString(),
                name = "Novo Amigo",
                initials = "NA",
                role = "Convidado",
                hasAccount = false,
            )
            state = state.copy(participants = state.participants + newP)
        },
        onSubmitClick = {
            onTripCreatedSuccess("trip-123")
        },
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

/**
 * Versão Stateless (Pura) com State Hoisting para máxima testabilidade e reutilização.
 */
@Composable
fun CreateTripContent(
    state: CreateTripUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onAddActivityClick: () -> Unit,
    onAddParticipantClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(horizontal = SpacingXL, vertical = SpacingLG)
                .padding(bottom = 100.dp), // Espaço para a barra de ação fixa
            horizontalAlignment = Alignment.Start,
        ) {
            // ----------------------------------------------------
            // 1. Barra Superior de Navegação
            // ----------------------------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Card(
                    shape = RoundedCornerShape(RadiusMD),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onBackClick() },
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "←", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                }

                Text(
                    text = "Cadastrar Viagem",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )

                Card(
                    shape = RoundedCornerShape(RadiusMD),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onBackClick() },
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "✕", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(SpacingLG))

            // ----------------------------------------------------
            // 2. Cabeçalho Informativo
            // ----------------------------------------------------
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(RadiusXL))
                    .background(Color(0xFFEFF6FF))
                    .padding(horizontal = SpacingMD, vertical = SpacingXS),
            ) {
                Text(
                    text = "✈️ NOVA VIAGEM EM GRUPO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlueOcean,
                )
            }

            Spacer(modifier = Modifier.height(SpacingSM))

            Text(
                text = "Para onde vamos?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )

            Spacer(modifier = Modifier.height(SpacingXS))

            Text(
                text = "Preencha as informações da viagem para começar a dividir gastos com os participantes.",
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 18.sp,
            )

            Spacer(modifier = Modifier.height(SpacingLG))

            // ----------------------------------------------------
            // 3. Card: Título e Descrição
            // ----------------------------------------------------
            Card(
                shape = RoundedCornerShape(RadiusLG),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(SpacingLG)) {
                    Text(
                        text = "Título da viagem *",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                    )

                    Spacer(modifier = Modifier.height(SpacingXS))

                    OutlinedTextField(
                        value = state.title,
                        onValueChange = onTitleChange,
                        placeholder = { Text("Ex: Férias em Floripa, Carnaval...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(RadiusMD),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlueOcean,
                            unfocusedBorderColor = BorderLight,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                        ),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(SpacingMD))

                    Text(
                        text = "Descrição (opcional)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                    )

                    Spacer(modifier = Modifier.height(SpacingXS))

                    OutlinedTextField(
                        value = state.description,
                        onValueChange = onDescriptionChange,
                        placeholder = { Text("Adicione um recado para o grupo ou anotações...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp),
                        shape = RoundedCornerShape(RadiusMD),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlueOcean,
                            unfocusedBorderColor = BorderLight,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                        ),
                        maxLines = 3,
                    )
                }
            }

            Spacer(modifier = Modifier.height(SpacingMD))

            // ----------------------------------------------------
            // 4. Card: Período e Datas
            // ----------------------------------------------------
            Card(
                shape = RoundedCornerShape(RadiusLG),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(SpacingLG)) {
                    Text(
                        text = "Período da viagem *",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                    )

                    Spacer(modifier = Modifier.height(SpacingSM))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(SpacingSM),
                    ) {
                        // Data de Início
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(RadiusMD))
                                .background(Color(0xFFF8FAFC))
                                .border(1.dp, BorderLight, RoundedCornerShape(RadiusMD))
                                .padding(SpacingMD),
                        ) {
                            Column {
                                Text("INÍCIO", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                Text(state.startDate.ifEmpty { "Selecione" }, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                        }

                        Text("→", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDisabled)

                        // Data de Fim
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(RadiusMD))
                                .background(Color(0xFFF8FAFC))
                                .border(1.dp, BorderLight, RoundedCornerShape(RadiusMD))
                                .padding(SpacingMD),
                        ) {
                            Column {
                                Text("TÉRMINO", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                Text(state.endDate.ifEmpty { "Selecione" }, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(SpacingSM))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(RadiusXL))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = SpacingSM, vertical = 2.dp),
                        ) {
                            Text("✓ 7 dias de viagem", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GreenTravel)
                        }

                        Text("Você pode alterar as datas depois", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(SpacingMD))

            // ----------------------------------------------------
            // 5. Card: Roteiro (Pode adicionar depois)
            // ----------------------------------------------------
            Card(
                shape = RoundedCornerShape(RadiusLG),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(SpacingLG)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Roteiro de Atividades",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(RadiusXL))
                                .background(Color(0xFFFFF7ED))
                                .border(1.dp, Color(0xFFFFEDD5), RoundedCornerShape(RadiusXL))
                                .padding(horizontal = SpacingSM, vertical = 2.dp),
                        ) {
                            Text(
                                text = "Pode adicionar depois",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OrangeAccent,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(SpacingXS))

                    Text(
                        text = "Atividades já programadas para a viagem:",
                        fontSize = 12.sp,
                        color = TextSecondary,
                    )

                    Spacer(modifier = Modifier.height(SpacingSM))

                    // Lista de Atividades
                    state.activities.forEach { activity ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(RadiusMD))
                                .background(Color(0xFFF8FAFC))
                                .border(1.dp, BorderLight, RoundedCornerShape(RadiusMD))
                                .padding(horizontal = SpacingMD, vertical = SpacingSM),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(activity.dateHour, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BlueOcean)
                                    Text(activity.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text("📍 ${activity.location}", fontSize = 11.sp, color = TextSecondary)
                                }

                                if (activity.estimatedCost != null) {
                                    Text(
                                        text = "R$ ${activity.estimatedCost.toInt()}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GreenTravel,
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(SpacingSM))

                    // Botão Adicionar Atividade
                    Button(
                        onClick = onAddActivityClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEFF6FF),
                            contentColor = BlueOcean,
                        ),
                        border = BorderStroke(1.dp, BlueOcean),
                        shape = RoundedCornerShape(RadiusMD),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("+ Adicionar atividade ao roteiro", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(SpacingXS))

                    Text(
                        text = "💡 Dica: Não definiu todos os passeios? Adicione durante a viagem.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(SpacingMD))

            // ----------------------------------------------------
            // 6. Card: Participantes (Pode adicionar depois)
            // ----------------------------------------------------
            Card(
                shape = RoundedCornerShape(RadiusLG),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(SpacingLG)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Participantes (${state.participants.size})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(RadiusXL))
                                .background(Color(0xFFF0FDF4))
                                .border(1.dp, Color(0xFFDCFCE7), RoundedCornerShape(RadiusXL))
                                .padding(horizontal = SpacingSM, vertical = 2.dp),
                        ) {
                            Text(
                                text = "Pode adicionar depois",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = GreenTravel,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(SpacingXS))

                    Text(
                        text = "Quem vai dividir os custos da viagem com você:",
                        fontSize = 12.sp,
                        color = TextSecondary,
                    )

                    Spacer(modifier = Modifier.height(SpacingSM))

                    // Lista de Participantes
                    state.participants.chunked(2).forEach { rowParticipants ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(SpacingSM),
                        ) {
                            rowParticipants.forEach { participant ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(RadiusMD))
                                        .background(Color(0xFFF8FAFC))
                                        .border(1.dp, BorderLight, RoundedCornerShape(RadiusMD))
                                        .padding(SpacingSM),
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(SpacingSM),
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(if (participant.isOrganizer) BlueOcean else OrangeAccent),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Text(
                                                text = participant.initials,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                            )
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = participant.name,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                            Text(
                                                text = participant.role,
                                                fontSize = 9.sp,
                                                color = if (participant.isOrganizer) BlueOcean else TextSecondary,
                                            )
                                        }
                                    }
                                }
                            }
                            if (rowParticipants.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(SpacingSM))

                    // Botão Adicionar Participante
                    Button(
                        onClick = onAddParticipantClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEFF6FF),
                            contentColor = BlueOcean,
                        ),
                        border = BorderStroke(1.dp, BlueOcean),
                        shape = RoundedCornerShape(RadiusMD),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("+ Convidar ou adicionar participantes", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(SpacingXS))

                    Text(
                        text = "ℹ️ Amigos sem o app instalado também podem ser adicionados.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                    )
                }
            }
        }

        // ----------------------------------------------------
        // 7. Rodapé Fixo Inferior com Botão Primário
        // ----------------------------------------------------
        Card(
            shape = RoundedCornerShape(topStart = RadiusXL, topEnd = RadiusXL),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, BorderLight),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = SpacingXL, vertical = SpacingMD),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = onSubmitClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(RadiusMD),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueOcean),
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Criar Viagem ✈️",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                }

                TextButton(onClick = onBackClick) {
                    Text(
                        text = "Salvar como rascunho",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                    )
                }
            }
        }
    }
}
