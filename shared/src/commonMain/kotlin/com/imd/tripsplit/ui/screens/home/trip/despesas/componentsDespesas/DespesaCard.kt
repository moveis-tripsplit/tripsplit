package com.imd.tripsplit.ui.screens.home.trip.despesas.componentsDespesas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.imd.tripsplit.model.Despesa
import com.imd.tripsplit.model.OrigemDespesa
import com.imd.tripsplit.model.Participant

@Composable
fun DespesaCard(
    despesa: Despesa,
    participantes: List<Participant>,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    val pagador =
        participantes.find {
            it.id == despesa.pagadorId
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = despesa.descricao,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = formatarMoeda(despesa.valorTotal),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            OrigemDespesaBadge(origem = despesa.origem)

            if (despesa.origem == OrigemDespesa.MANUAL) {
                Text(
                    text = "Pago por: ${pagador?.name ?: "Não informado"}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "${despesa.participantesIds.size} participante(s) na divisão",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onEditar) {
                        Text("✎  Editar")
                    }

                    Spacer(modifier = Modifier.size(8.dp))

                    OutlinedButton(onClick = onExcluir) {
                        Text("×  Excluir")
                    }
                }
            } else {
                Text(
                    text = "Despesa vinculada ao roteiro da viagem",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun OrigemDespesaBadge(origem: OrigemDespesa) {
    val texto =
        when (origem) {
            OrigemDespesa.MANUAL -> "Manual"
            OrigemDespesa.ROTEIRO -> "Gerada pelo roteiro"
        }

    val cor =
        when (origem) {
            OrigemDespesa.MANUAL -> MaterialTheme.colorScheme.primaryContainer
            OrigemDespesa.ROTEIRO -> MaterialTheme.colorScheme.tertiaryContainer
        }

    Surface(
        color = cor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = texto,
            modifier =
                Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 5.dp
                ),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

internal fun formatarMoeda(valor: Double): String {
    return "R$ " +
        kotlin.math.round(valor * 100)
            .div(100)
            .toString()
            .replace(".", ",")
}
