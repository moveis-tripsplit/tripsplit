package com.imd.tripsplit.presentation.trip.expenses

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.imd.tripsplit.model.Despesa
import com.imd.tripsplit.model.Participant

@Composable
fun TelaDeDespesasScreen(
    nomeViagem: String,
    despesas: List<Despesa>,
    participantes: List<Participant>,
    totalViagem: Double,
    meuSaldo: Double,
    onVoltar: () -> Unit,
    onAdicionarDespesa: () -> Unit,
    onEditarDespesa: (Despesa) -> Unit,
    onExcluirDespesa: (Despesa) -> Unit,
    tripId: String,
    onSalvarDespesa: (Despesa) -> Unit,
    modifier: Modifier = Modifier
) {
    TelaDeDespesasContent(
        nomeViagem = nomeViagem,
        despesas = despesas,
        participantes = participantes,
        totalViagem = totalViagem,
        meuSaldo = meuSaldo,
        onVoltar = onVoltar,
        onAdicionarDespesa = onAdicionarDespesa,
        onEditarDespesa = onEditarDespesa,
        onExcluirDespesa = onExcluirDespesa,
        tripId = tripId,
        onSalvarDespesa = onSalvarDespesa,
        modifier = modifier
    )
}
