package com.imd.tripsplit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.ui.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(
    tripId: String,
    onBackClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onAddParticipantClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Aqui você receberia a viagem pelo ID (via ViewModel ou parâmetro)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da Viagem") },
                // Você pode adicionar um botão de voltar aqui
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Seção de Participantes
            Text(style = androidx.compose.material3.MaterialTheme.typography.titleMedium, text = "Participantes")
            PrimaryButton(
                text = "+ Adicionar Participante",
                onClick = onAddParticipantClick,
                modifier = Modifier.fillMaxWidth()
            )

            // Seção de Atividades / Gastos
            Text(style = androidx.compose.material3.MaterialTheme.typography.titleMedium, text = "Gastos e Atividades")
            PrimaryButton(
                text = "+ Adicionar Gasto",
                onClick = onAddExpenseClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}