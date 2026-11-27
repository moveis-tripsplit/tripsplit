
package com.imd.tripsplit.presentation.trip.expenses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.imd.tripsplit.model.Despesa
import com.imd.tripsplit.model.OrigemDespesa
import com.imd.tripsplit.model.Participant
import com.imd.tripsplit.ui.screens.home.trip.despesas.componentsDespesas.DespesaCard
import com.imd.tripsplit.ui.screens.home.trip.despesas.componentsDespesas.ModalCadastroDespesa
import com.imd.tripsplit.ui.screens.home.trip.despesas.componentsDespesas.ResumoFinanceiro
import com.imd.tripsplit.ui.theme.BackgroundLight
import com.imd.tripsplit.ui.theme.BlueOcean
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingSM
import com.imd.tripsplit.ui.theme.TextPrimary
import com.imd.tripsplit.ui.theme.TextSecondary
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDeDespesasContent(
    nomeViagem: String,
    despesas: List<Despesa>,
    participantes: List<Participant>,
    totalViagem: Double,
    meuSaldo: Double,
    onVoltar: () -> Unit,
    onAdicionarDespesa: () -> Unit,
    onEditarDespesa: (Despesa) -> Unit,
    onExcluirDespesa: (Despesa) -> Unit,
    tripId: String = "",
    onSalvarDespesa: (Despesa) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var mostrarModal by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundLight,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Despesas $nomeViagem",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Text(
                        text = "←",
                        color = TextPrimary,
                        modifier =
                            Modifier
                                .padding(horizontal = SpacingMD)
                                .clickable { onVoltar() }
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = BackgroundLight,
                        scrolledContainerColor = BackgroundLight
                    )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    mostrarModal = true
                    onAdicionarDespesa()
                },
                containerColor = BlueOcean,
                contentColor = Color.White
            ) {
                Text(
                    text = "+ Cadastrar despesa",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->

        BoxWithConstraints(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
        ) {
            val isWideScreen = maxWidth >= 600.dp

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = if (isWideScreen) SpacingSM else SpacingLG
                        )
                        .widthIn(max = 840.dp)
                        .align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(SpacingLG)
            ) {
                // Resumo financeiro
                if (isWideScreen) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(SpacingMD)
                    ) {
                        ResumoFinanceiro(
                            titulo = "Total da viagem",
                            valor = totalViagem,
                            modifier = Modifier.weight(1f)
                        )

                        ResumoFinanceiro(
                            titulo = "Meu saldo",
                            valor = meuSaldo,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(SpacingMD)
                    ) {
                        ResumoFinanceiro(
                            titulo = "Total da viagem",
                            valor = totalViagem,
                            modifier = Modifier.fillMaxWidth()
                        )

                        ResumoFinanceiro(
                            titulo = "Meu saldo",
                            valor = meuSaldo,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Text(
                    text = "Despesas cadastradas",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                if (despesas.isEmpty()) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Nenhuma despesa cadastrada",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Adicione uma despesa para começar a organizar os gastos da viagem.",
                            color = TextSecondary
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(SpacingMD),
                        contentPadding = PaddingValues(bottom = 88.dp)
                    ) {
                        items(
                            items = despesas,
                            key = { it.id }
                        ) { despesa ->
                            DespesaCard(
                                despesa = despesa,
                                participantes = participantes,
                                onEditar = { onEditarDespesa(despesa) },
                                onExcluir = { onExcluirDespesa(despesa) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal de cadastro
    if (mostrarModal) {
        ModalCadastroDespesa(
            participantes = participantes,
            onFechar = {
                mostrarModal = false
            },
            onSalvar = { descricao, valor, pagadorId, participantesIds ->

                val novaDespesa =
                    Despesa(
                        id = Random.nextLong().toString(),
                        tripId = tripId,
                        descricao = descricao,
                        valorTotal = valor,
                        pagadorId = pagadorId,
                        participantesIds = participantesIds,
                        pagamentos = emptyList(),
                        origem = OrigemDespesa.MANUAL,
                        data = ""
                    )

                onSalvarDespesa(novaDespesa)
                mostrarModal = false
            }
        )
    }
}
